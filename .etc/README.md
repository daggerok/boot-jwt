generate cert
=============

```bash
$ keytool -genkeypair -alias bootjwt -keyalg RSA -keystore .etc/boot-jwt.jks \
          -dname "CN=boot-jwt, L=Odessa, S=Odessa, C=UA" \
          -keypass boot-jwt -storepass boot-jwt
$ keytool -list -rfc --keystore .etc/boot-jwt.jks | openssl x509 -inform pem -pubkey
Enter keystore password:  boot-jwt
-----BEGIN PUBLIC KEY-----
...skipped...
-----END PUBLIC KEY-----
-----BEGIN CERTIFICATE-----
...skipped...
-----END CERTIFICATE-----
# move public key into ./etc/public.cert file
```
