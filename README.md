gen key: keytool -genkey -keyalg RSA -keystore SSLStore -alias SSLCertificate

review: keytool -list -y  -keystore SSLStore

change pass: keytool -storepasswd -keystore SSLDStore

exprt: keytool -export -rfc -alias sslcertificate -keystore SSLStore -file mycert.cer

imprt: keytool -import -alias sslcertificate -file mycert.cer -keystore truststore