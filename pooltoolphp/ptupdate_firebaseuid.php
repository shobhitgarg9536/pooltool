<?php
require "ptconnection.php";
$mobileNo = $_POST["mobileNo"];
$firebaseUid = $_POST["firebaseUid"];
$mysql_qry = "UPDATE register SET firebaseUid = '$firebaseUid' WHERE mobileNo = '$mobileNo'";
if($conn->query($mysql_qry) === TRUE){
	echo "INSERT SUCCESSFULL";
}
	else{
		echo "Error:".$mysql_qry."<br>".$conn->error;
}
$conn->close();
?>