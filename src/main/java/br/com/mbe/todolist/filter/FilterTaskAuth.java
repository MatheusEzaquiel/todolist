package br.com.mbe.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.mbe.todolist.domain.user.IUserRepository;
import br.com.mbe.todolist.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

	@Autowired
	IUserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String pathServlet = request.getServletPath();
		
		System.out.println(pathServlet);

		if (pathServlet.startsWith("/tasks")) {
			
			System.out.println("entrou em task");

			// Pegar a autenticação (usuário e senha)

			String authorization = request.getHeader("Authorization");

			String authEncoded = authorization.substring("Basic".length()).trim();

			byte[] authDecode = Base64.getDecoder().decode(authEncoded);

			String authString = new String(authDecode);

			String[] credentials = authString.split(":");

			String username = credentials[0];
			String password = credentials[1];

			// Validar usuário
			User user = userRepository.findByUserName(username);

			if (user == null) {

				response.sendError(401);

			} else {

				// Validar senha
				var PasswordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

				if (PasswordVerify.verified) {
					
					request.setAttribute("idUser", user.getId());
					
					// Continuar...
					filterChain.doFilter(request, response);

				} else {
					response.sendError(401);
				}

			}

		} else {
			filterChain.doFilter(request, response);
		}

	}

}
