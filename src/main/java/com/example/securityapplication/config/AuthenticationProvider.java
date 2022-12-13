package com.example.securityapplication.config;

//@Component
//public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
//    private final PersonDetailsService personDetailsService;
//    @Autowired
//    public AuthenticationProvider(PersonDetailsService personDetailsService) {
//        this.personDetailsService = personDetailsService;
//    }

//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String login = authentication.getName();
//        UserDetails person = personDetailsService.loadUserByUsername(login);
//        String password = authentication.getCredentials().toString();
//        if (!password.equals(person.getPassword())){
//            throw new BadCredentialsException("Не корректный пароль!");
//        }
//        return new UsernamePasswordAuthenticationToken(person, password, Collections.emptyList());
//    }

//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//}
