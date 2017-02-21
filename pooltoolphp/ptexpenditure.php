<?php
    include_once 'pt_function.php';
   
	$uid = $_POST["uid"];
	$toMobileNo = $_POST["toMobileNo"];
	$amount = $_POST["amount"];
	$groupId = $_POST["groupId"];
	$date = $_POST["date"];
	$time = $_POST["time"];
$mysql_qry = "insert into item_detail(uid,toMobileNo,amount,groupId,date,time)values('$uid','$toMobileNo','$amount','$groupId','$date','$time')";
if($conn->query($mysql_qry) === TRUE){
	echo "INSERT SUCCESSFULL";
}
	else{
		echo "Error:".$mysql_qry."<br>".$conn->error;
}
$conn->close();
?>