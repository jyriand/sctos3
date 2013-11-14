<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
	<script>
        $(function(){
            $(".sendSong").click(function(){
                console.log('This button clikked')
                var button = $(this);
                var id = $(this).attr('id');
                var user = $(this).attr('user');

                var url = '/saveSong?username=' + user + '&songId='+id;
                $.get(url, function(data){
                    if(data.success){
                        button.replaceWith("SENDING");
                    }else{
                        alert("Failed");
                    }
                });
            });
        });
	</script>

</head>
<body style="margin-left:60px;">
<h1>
	Soundclub to Amazon S3 importer
</h1>
<hr>
<form id="userSearchForm" action="/searchUserSongs" method="get">
	<input name="username" type="text">
	<button type="submit">Search User</button>
</form>

<table>
	<tr>
		<th>Song name</th>
		<th>Duration</th>
		<th>Action</th>
	</tr>
	<c:if test="${not empty userSongs}">
	<c:forEach var="song" items="${userSongs}">
        <tr>
            <td>${song.id}</td>
            <td>3:00</td>
            <td><button class="sendSong" id="${song.id}" user="${username}">Send to S3</button></td>
        </tr>
	</c:forEach>
	</c:if>
</table>

</body>
</html>
