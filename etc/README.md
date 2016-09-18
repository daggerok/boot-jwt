generate cert
=============

auth server will use boot-jwt.jks certificate for JWT tokens. let's create it:

```bash
$ keytool -genkeypair -alias bootjwt -keyalg RSA -keystore etc/boot-jwt.jks \
          -dname "CN=boot-jwt, L=Odessa, S=Odessa, C=UA" \
          -keypass boot-jwt -storepass boot-jwt
```

copy it resources folder of authorization-server:


```bash
$ mkdir authorization-server/src/main/resources/etc
$ cp etc/boot-jwt.jks authorization-server/src/main/resources/
```

protected clients will use only public key of previously created certificate. lets fetch public key: 

```bash
$ keytool -list -rfc --keystore etc/boot-jwt.jks | openssl x509 -inform pem -pubkey
Enter keystore password:  boot-jwt
-----BEGIN PUBLIC KEY-----
...skipped...
-----END PUBLIC KEY-----
-----BEGIN CERTIFICATE-----
...skipped...
-----END CERTIFICATE-----
```

now put public key (including BEGIN and END lines) into public.cert file, and copy it in resources folder of resource-server:

```bash
$ mkdir -p resource-server/src/main/resources/etc
$ cp etc/public.cert resource-server/src/main/resources/etc
```

