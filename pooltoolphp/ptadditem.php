<?php
    include_once 'pt_function.php';
   
	$mobileNo = $_POST["mobileNo"];
	$groupId = $_POST["groupId"];
	$itemId = $_POST["itemId"];
	$item = $_POST["item"];
	$money = $_POST["money"];
	$equalOrCustomise = $_POST["equalOrCustomise"];
	$date = $_POST["date"];
	$time = $_POST["time"];
$mysql_qry = "insert into item_detail(mobileNo,groupId,itemId,item,money,equalOrCustomise,date,time)values('$mobileNo','$groupId','$itemId','$item','$money','$equalOrCustomise','$date','$time')";
if($conn->query($mysql_qry) === TRUE){
	echo "INSERT SUCCESSFULL";
}
	else{
		echo "Error:".$mysql_qry."<br>".$conn->error;
}
$conn->close();
?>