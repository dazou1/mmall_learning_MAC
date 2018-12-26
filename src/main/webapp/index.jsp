<html>
<body>
<h2>Tomcat1!</h2>
<h2>Tomcat1!</h2>
<h2>Tomcat1!</h2>
<h2>Hello World!</h2>
<h2>Dazou</h2>
<%@page pageEncoding="UTF-8"%>

SpringMVC上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="SpringMVC上传文件"/>
</form>


富文本图片上传
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="SpringMVC富文本图片上传"/>
</form>

</body>
</html>
