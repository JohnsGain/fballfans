package com.john.auth.utils;

import com.john.auth.domain.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjuwa
 * @date 2019/2/26
 * @since jdk1.8
 */
public class JwtTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    // 寻找证书文件
    private static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("marsttssl.jks"); // 寻找证书文件
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, "123456".toCharArray());
            privateKey = (PrivateKey) keyStore.getKey("certificatekey", "123456".toCharArray());
            publicKey = keyStore.getCertificate("certificatekey").getPublicKey();

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成token
     *
     * @param subject           （主体信息）
     * @param expirationSeconds 过期时间（秒）
     * @param claims            自定义身份信息
     * @return
     */
    public static String generateToken(String subject, int expirationSeconds, Map<String, Object> claims) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuer("iss")
                .setAudience("aud")
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {
        Claims tokenBody = null;
        try {
            tokenBody = getTokenBody(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return tokenBody.getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public static void main(String[] args) {
        SysUser sysUser = new SysUser();
        sysUser.setId(1L);
        sysUser.setNickname("abc");
        sysUser.setUsername("john");
        sysUser.setRemark("fddff");
        Map<String, Object> map = new HashMap<>(8);
        map.put("username", sysUser.getUsername());
        map.put("nickname", sysUser.getNickname());
        map.put("id", sysUser.getId());
        map.put("remark", sysUser.getRemark());
        String john = generateToken("john", 10, map);
        LOGGER.info("jwt={}", john);
        //String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJuaWNrbmFtZSI6ImFiYyIsInJlbWFyayI6ImZkZGZmIiwiaWQiOjEsInVzZXJuYW1lIjoiam9obiIsInN1YiI6ImpvaG4iLCJleHAiOjE1NTExNjgwMjF9.OX0RauaOG_d_iiWiQ58tXGTJFgjq6Ub2nLP51DhfrfpRM431xrVlsu1l87aCjwwZKUTEq0UBAqkJewUKaZbhcXn7HYSY-CO17ouP4NwQaE51g6DkV7o_LEuTlTQ0rZE7RAqbZ_pfnOVH4tZ6L399vIssGVX0x16qv_0okmbxT5zUKKc7Nu6llQu6Qwa7LUvkdHd21gwL0zjpOJ__-WngBpNf_WOi5A9IRyjOwL1ig0d3aaKYqzt-rbwmEIPXL0_QA1YDrY-zf5vO6zplZ2bWAwFWxAAjE5iflfzrGbrhBpeEou-edbIY4f-_Qu19liavU9-3GB9pk4n4gTMtzslrQA";
        //System.out.println(isExpiration(jwt));
        //System.out.println(Jwts.parser().setSigningKey(publicKey).isSigned(jwt));;
    }

    private static Map<String, Object> bean2map(Object bean) {
        Map<String, Object> map = new HashMap<>(16);
        new BeanMap(bean).forEach((k, v) -> map.put(String.valueOf(k), v));
        return map;
    }
}
