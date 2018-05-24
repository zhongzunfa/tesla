# 获取授权码(GET)
http://localhost:8080/oauth/authorize?response_type=code&scope=read write&client_id=test&redirect_uri=http://localhost:8080/oauth/default_redirect_url&state=09876999

# 获取Token(POST)
http://localhost:8080/oauth/token?client_id=test&client_secret=test&grant_type=authorization_code&code=65d5b770d3ead3276fe4f37ee253ed19&redirect_uri=http://localhost:8080/oauth/default_redirect_url