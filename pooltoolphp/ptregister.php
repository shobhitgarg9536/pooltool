<?php
require "ptconnection.php";
$mobileNo = $_POST["mobileNo"];
$name = $_POST["name"];
$countryCode = $_POST["countryCode"];
$email = $_POST["email"];
$firebaseUid = $_POST["firebaseUid"];
$mysql_qry = "insert into register(mobileNo,name,countryCode,email,firebaseUid)values('$mobileNo','$name','$countryCode','$email','$firebaseUid')";
if($conn->query($mysql_qry) === TRUE){
	echo "INSERT SUCCESSFULL";
}
	else{
		echo "Error:".$mysql_qry."<br>".$conn->error;
}
$conn->close();
?>