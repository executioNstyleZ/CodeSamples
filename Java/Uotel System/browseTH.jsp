<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="cs5530.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script>
function check_all_fields(form){
	
	if((form.lowerValue.value != "" && form.upperValue.value == "") || 
			(form.lowerValue.value == "" && form.upperValue.value != "")){
		alert("Please fill out both price ranges");
		return false;
	}
	
	if( form.sortingValue.value == ""){
		alert("You must choose how to sort the results");
		return false;
	}
	return true;
}
</script>
<title>Browse Temporary Housing</title>
</head>
<body>

	<h1>Browse TH</h1>


		<%
		String sorting = request.getParameter("sortingValue");
		
		if(sorting == null){
		%>

			Please fill out the fields you would like to search by:<p>
			Please note that results must be sorted by: <p>
			1. by price<p>
			2. by the average numerical score of the feedbacks<p>
			3. by the average numerical score of the trusted user feedbacks<p>
			<br>

			<form name="searchForm" method=get onsubmit="return check_all_fields(this)" action="browseTH.jsp">
				<input type=text name="lowerValue" length=10 placeholder="lower price">
				<input type=text name="upperValue" length=10 placeholder="upper price">
				<input type=text name="cityValue" length=10 placeholder="city">
				<input type=text name="keywordValue" length=10 placeholder="keyword">
				<input type=text name="categoryValue" length=10 placeholder="category">
				<input type=text name="sortingValue" length=1 placeholder="sorted by">
				<input type=submit class="search" value="search">
			</form>
			<BR>
			<%
		}else{
			
			String lower = request.getParameter("lowerValue");
			String upper = request.getParameter("upperValue");
			String city = request.getParameter("cityValue");
			String keyword = request.getParameter("keywordValue");
			String category = request.getParameter("categoryValue");
			
			int lowerInt = 0;
			int upperInt = 10000;
			int sortInt = 4;
			
			try{
				lowerInt = Integer.parseInt(lower);
				upperInt = Integer.parseInt(upper);
				sortInt = Integer.parseInt(sorting);
			}catch(Exception e){
				
			}
			Connector con = new Connector();
			Housing th = new Housing();
			String results = th.browseTemporaryHousing(lowerInt, upperInt, city, keyword, category, sortInt, con.stmt);
			//display results	
		}
			%>

<a href="thIndex.html">back to TH menu</a><br>

</body>
</html>