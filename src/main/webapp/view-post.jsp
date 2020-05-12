<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="foo.A_ConnexionServlet" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Post Example</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">


<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
<script defer
	src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>


<script src="https://unpkg.com/mithril/mithril.js"></script>

</head>
<body>

<jsp:useBean id="connect" scope="request" class="foo.A_ConnexionServlet" />
    
    <c:if test="<%= connect.getConnected() == false %>"> 

    	<%
       RequestDispatcher rd = request.getRequestDispatcher("/");
       rd.forward(request, response);
		%>
    	
    </c:if>
    
    <c:if test="<%= connect.getConnected() == true %>"> 
	<script>	

// for the example
var me="<%= connect.getNicknameUser() %>";

var MyPost = {
		list: [],
	    nextToken: "",
	    loadList: function() {
	        return m.request({
	            method: "GET",
	            url: "_ah/api/myApi/v1/mypost/"+me})
	        .then(function(result) {
	        	console.log("got:",result)
	        	MyPost.list=result.items
	            if ('nextPageToken' in result) {
		        	MyPost.nextToken= result.nextPageToken
	            } else {
	            	MyPost.nextToken=""
	            }})
	    },
	    next: function() {
	        return m.request({
	            method: "GET",
	            url: "_ah/api/myApi/v1/mypost/"+me+"?next="+MyPost.nextToken})
	        .then(function(result) {
	        	console.log("got:",result)
	        	result.items.map(function(item){MyPost.list.push(item)})
	            if ('nextPageToken' in result) {
		        	MyPost.nextToken= result.nextPageToken
	            } else {
	            	MyPost.nextToken=""
	            }})
	    },
 	    postMessage: function() {
 			var data={'owner':me,
 					'url':PostForm.url,
 					'body':PostForm.body}
 	    	console.log("post:"+data)
     		return m.request({
         		method: "POST",
         		url: "_ah/api/myApi/v1/postMessage",
             	params: data,
         	})
  	    	.then(function(result) {
     	 			console.log("got:",result),
     	 			MyPost.loadList()
         	 	})
     	}
}

var PostView = {
  oninit: MyPost.loadList,
  view: function() {
   	return m('div', [
	  m('div',{class:'subtitle'},"My Posts"),
	  m('table', {class:'table is-striped',"table":"is-striped"},[
	    m('tr', [
		  m('th', {width:"50px"}, "like"),
		  m('th', {width:"50px"}, "del"),
	      m('th', {width:"50px"}, "Bodys"),
	      m('th', {width:"50px"}, "Urls"),
	      m('th', {width:"50px"}, "Like"),
	    ]),
	    MyPost.list.map(function(item) {
	      return m("tr", [
            m("td", m("button", {onclick: function(e) {
				console.log("like:"+item.key.id)
                 }},"like")),
                 m("td", m("button", {onclick: function(e) {
     				console.log("del:"+item.key.id)
                 }},"del")),
	        m('td', m('label', item.properties.body)),
	        m('td', m('img', {class: 'is-rounded', 'src': item.properties.url})),
	        m('td', m('label', item.properties.likec)),
	      ])
	    }),
//	    m("div", isError ? "An error occurred" : "Saved"),
	    m('button',{
		      class: 'button is-link',
		      onclick: function(e) {MyPost.next()}
		      },
		  "Next"),
	   ])
	 ])
  }
}

var Hello = {
   view: function() {
   	return m('div', {class:'container'}, [
           m("h1", {class: 'title'}, 'The TinyGram Post'),
           m('div',{class: 'tile is-ancestor'},[
        	   m("div", {class: 'tile'}, m('div',{class:'tile is-child box'},m(PostView)))
           ])
       ])
   }
}


m.mount(document.body, Hello)	


</script>

</c:if>
</body>
</html>