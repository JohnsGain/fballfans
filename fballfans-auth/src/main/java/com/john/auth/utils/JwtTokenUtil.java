package com.john.auth.utils;

import com.alibaba.fastjson.JSON;
import com.john.auth.dto.SysUserOutput;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSigner;
import io.jsonwebtoken.impl.crypto.JwtSigner;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
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
                .signWith(privateKey, getAlgorithm())
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

    /**
     * 使用这种方式获取claims,如果jwt已经过期，会抛出异常
     * @param token
     * @return
     */
    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取加密算法
     */
    private static SignatureAlgorithm getAlgorithm() {
        return SignatureAlgorithm.RS256;
    }

    public static boolean isSignKey(String token) {
        //JwtSigner signer = new DefaultJwtSigner(getAlgorithm(), );
        return Jwts.parser()
                .setSigningKey(publicKey)
                .isSigned(token);
    }

    public static void main(String[] args) {
        String deo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZENhcmQiOiIiLCJhdXRoU3RhdHVzIjoi6K6k6K-B5LitIiwicm9sZXMiOlsiTkVUX1VTRVIiXSwiZnVsbE5hbWUiOiLlk4jlk4jlk4giLCJ1c2VySWQiOiJlMzUwYTdjODYyMTI0ZTE3YWE4OWI2ODM0ODRiN2JmYyIsImNlbGxwaG9uZSI6IjE3Nzg1MjcyMTg3IiwicGVybXMiOlsicGVyc29uYWw6KiJdLCJjbGFzcyI6ImNvbS5kZGIuY29tbW9uLnVzZXIuTG9naW5Vc2VyIiwianRpIjoiOTA5NjQ5MjcyNDYzMDQ1NSIsInVzZXJuYW1lIjoiMTc3ODUyNzIxODciLCJpc3MiOiJEREIiLCJhdWQiOiJHQVRFV0FZIiwic3ViIjoiMTc3ODUyNzIxODciLCJleHAiOjE1NTIxMjUxMDYsIm5iZiI6MTU1MTI2MTEwNn0.us48ysis_5IIInfzQB9sNn0U4qIIYJrdp6RK65bx_jo";
        Claims tokenBody = getTokenBody(deo);
        System.out.println(isSignKey(deo));
    }

    public static SysUserOutput getUserInfo(String jwt) {
        if (StringUtils.isBlank(jwt)) {
            return null;
        }
        jwt = jwt.replace("Bearer ", "");
        String[] split = jwt.split("\\.");
        if (split.length < 3) {
            return null;
        }
        String payload = new String(Base64.decodeBase64(split[1]));
        return JSON.parseObject(payload, SysUserOutput.class);
    }

    private static Map<String, Object> bean2map(Object bean) {
        Map<String, Object> map = new HashMap<>(16);
        new BeanMap(bean).forEach((k, v) -> map.put(String.valueOf(k), v));
        return map;
    }

}
