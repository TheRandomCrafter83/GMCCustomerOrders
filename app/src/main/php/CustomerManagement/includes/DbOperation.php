<?php
 
class DbOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }
	
	/*
	* The create operation
	* When this method is called a new record is created in the database
	*/
	function createUser($username, $password, $isadmin){
        $stmt = $this->con->prepare("INSERT INTO Users (Username, Password, IsAdmin) VALUES (?, ?, ?)");
        $pw = md5($password);
		$stmt->bind_param("ssi", $username, $pw, $isadmin);
		if($stmt->execute())
			return true; 
		return false; 
	}
 
	/*
	* The read operation
	* When this method is called it is returning all the existing record of the database
	*/
	function getUsers(){
        $stmt = $this->con->prepare("SELECT UserID, Username, IsAdmin FROM Users")
        $stmt->execute();
        $stmt->bind_result($userid,$username,$isadmin);
        $users = array();
        while($stmt->fetch()){
            $user = array();
            $user['userid'] = $userid;
            $user['username'] = $username;
            $user['isadmin'] = $isadmin;
            array_push($users, $user);
        }
		return $users; 
	}
	
	/*
	* The update operation
	* When this method is called the record with the given id is updated with the new given values
	*/
	function updateUser($userid, $username, $password, $isadmin){
        $stmt = $this->con->prepare("UPDATE Users SET Username = ?, Password = ?, IsAdmin = ? WHERE UserID = ?");
        $pw = md5($password);
		$stmt->bind_param("ssii", $username, $pw, $isadmin, $userid);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	
	/*
	* The delete operation
	* When this method is called record is deleted for the given id 
	*/
	function deleteUser($userid){
		$stmt = $this->con->prepare("DELETE FROM Users WHERE UserID = ? ");
		$stmt->bind_param("i", $userid);
		if($stmt->execute())
			return true; 
		
		return false; 
    }
    
    /*
    * The login function
    * When this method is called, the system will check first to see if the username exists, then it will check and compare the given password
    */
    function loginUser($username, $password){
        $pw = md5($password);
        $stmt = $this->con->prepare("SELECT UserID = ? FROM Users WHERE Username = ? AND Password = ?");
        $stmt->bind_param("ss",$username,$pw)
        if ($stmt->execute()){
            $stmt->bind_result($userid)
            $result['error'] = false;
            $result['message'] = "User successfully logged in."
            $result['userid'] = $userid;
        }else{
            $result = array();
            $result['error'] = true;
            $result['message'] = 'Invalid username and/or password.';
            $result['userid'] = 0
        }
        return $result;
    }

    function getUser($userid){
        $stmt = $this->con->prepare("SELECT Username = ?, IsAdmin = ?, UserID = ? FROM Users WHERE UserID = ?");
        $stmt->bind_param("i",$userid)
        $stmt->execute();
        $stmt->bind_result($username,$isadmin,$userid);
        $result['username'] = $username;
        $result['isadmin'] = $isadmin;
        $result['userid'] = $userid;
        return $result
    }
}