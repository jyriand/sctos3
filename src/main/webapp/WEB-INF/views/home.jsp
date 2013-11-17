<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
	<script>
        $(function(){
            $(".sendTrack").click(function(){
                console.log('This button clikked')
                var button = $(this);
                var id = $(this).attr('id');
                var user = $(this).attr('user');

                var url = '/saveTrack?username=' + user + '&trackId='+id;
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
<body style="margin-left:60px; margin-bottom:40px;">
<h1>
	Soundclub to Amazon S3 importer
</h1>
<hr>

<form id="userSearchForm" action="/searchUserTracks" method="get">
	<input name="username" type="text">
	<button type="submit">Search User</button>
	<c:if test="${not empty userError}"><span style="color:red;">${userError}</span></c:if>
</form>

<c:if test="${not empty username}">
    <b>Username:</b> ${username}
</c:if>
<c:if test="${empty userTracks and not empty username}">
    <p>This user doesnt have tracks</p>
</c:if>
<c:if test="${not empty userTracks}">
<br>
<table>
	<tr>
		<th>Track title</th>
		<th>Format</th>
		<th>Downloadable ?</th>
		<th>Action</th>
	</tr>
	<c:forEach var="track" items="${userTracks}">
        <tr>
            <td>${track.title}</td>
            <td>${track.format}</td>
            <td>
                <c:choose>
                    <c:when test="${track.downloadable}">YES</c:when>
                    <c:otherwise>NO</c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:if test="${track.downloadable}">
                    <button class="sendTrack" id="${track.id}" user="${username}">Copy to S3</button>
                </c:if>
                </td>
        </tr>
	</c:forEach>
</table>
</c:if>

</body>
</html>
