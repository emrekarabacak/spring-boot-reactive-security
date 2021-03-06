package com.ekarabacak.reactivesecurity;

import com.ekarabacak.reactivesecurity.model.Role;
import com.ekarabacak.reactivesecurity.model.User;
import com.ekarabacak.reactivesecurity.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ReactiveSecurityApplication {

    public static void main(String[] args) {
         SpringApplication.run(ReactiveSecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository userRepository, PasswordEncoder encoder){
        return args -> {

            Set<Role> roleSet = new HashSet<>();
            roleSet.add(Role.USER);

            User user1 = new User(null,"emre",encoder.encode("123"),roleSet);
            User user2 = new User(null,"elif",encoder.encode("123"),roleSet);


            userRepository.deleteAll()
                    .thenMany(userRepository.save(user1))
                    .thenMany(userRepository.save(user2))
                    .thenMany(userRepository.findAll())
                    .subscribe(users -> System.out.println(users));

        };
    }
}
