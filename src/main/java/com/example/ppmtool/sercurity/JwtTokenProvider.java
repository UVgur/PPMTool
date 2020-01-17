package com.example.ppmtool.sercurity;


import com.example.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.ppmtool.sercurity.SecurityConstants.EXPIRATION_TIME;
import static com.example.ppmtool.sercurity.SecurityConstants.SECRET;


@Component
public class JwtTokenProvider {

        public String generateToken(Authentication authentication){
            User user = (User)authentication.getPrincipal();
            Date now = new Date(System.currentTimeMillis());
            Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

            String userId = Long.toString(user.getId());
            Map<String, Object> claimsMap = new HashMap<>();
            claimsMap.put("id", (Long.toString(user.getId())) );
            claimsMap.put("username", user.getUsername() );
            claimsMap.put("fullName", user.getFullName() );



            return Jwts.builder().setSubject(userId)
                    .setClaims(claimsMap)
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();
        }

        public boolean validateToken(String token) {
            try{
                Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
                return true;
            }catch (SignatureException ex) {
                System.out.println("Invalid JWT Signature");
            } catch (MalformedJwtException ex) {
                System.out.println("Invalid JWT Token");
            }catch(ExpiredJwtException ex) {
                System.out.println("Expired JWT Token");
            }catch (UnsupportedJwtException ex){
                System.out.println("UnSupported JWT Token");
            }catch (IllegalArgumentException ex){
                System.out.println("JWT claims string is empty");
            }
            return false;
        }

        public Long getUserIdFromJWT(String token){
            Claims claims =Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            String id =(String)claims.get("id");

            return Long.parseLong(id);
        }
}
