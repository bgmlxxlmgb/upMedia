<%--
  Created by IntelliJ IDEA.
  User: bgm
  Date: 2015/11/26
  Time: 19:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <form action="/receive"method="post"enctype="multipart/form-data">
    <blockquote>
      <label>选择文件：</label>
      <select name="type">
        <option value="image">图片</option>
        <option value="voice">语音</option>
        <option value="video">视频</option>
        <option value="thumb">缩略图</option>
      </select>
    </blockquote>
    <blockquote>
      <label></label>
      <input type="file" name="part">
    </blockquote>
    <blockquote>
      <input type="submit">
      <input type="reset">
    </blockquote>
  </form>
  </body>
</html>
