# Introduction #

Add your content here.

# Subclipse #
%APPDATA%/Subversion/servers
```
[groups]
chenance = chenance.googlecode.com

[chenance]
http-proxy-host = xxx.xxx.xxx.xxx
http-proxy-port = yyyy
```

# m2eclipse #
%HOME%/.m2/settings.xml
```
<settings>
  <proxies>
    <proxy>
      <active>yes</active>
      <protocol>http</protocol>
      <username></username>
      <password></password>
      <port>yyyy</port>
      <host>xxx.xxx.xxx.xxx</host>
      <id/>
    </proxy>
  </proxies>
</settings>
```