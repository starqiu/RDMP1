<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录-集成R语言的数据挖掘平台</title>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/signin.css" rel="stylesheet">
  </head>
  <body>
    <div class="container">
      <form class="form-signin" role="form" action="welcome.do" method="post">
        <h2 class="form-signin-heading">请登录或注册</h2>
        <input id="userName" name="userName" type="text" class="form-control" placeholder="邮箱" required autofocus>
        <input type="password" class="form-control" placeholder="密码" required>
        <label class="checkbox">
          <input type="checkbox" value="remember-me"> 记住我
        </label>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
        <button class="btn btn-lg btn-primary btn-block" >注册</button>
      </form>
    </div>
    <script src="js/jquery-1.11.0.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
  </body>
</html>