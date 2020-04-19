<?php 
 
	//getting the dboperation class
	require_once '../includes/DbOperation.php';
	require_once '../includes/Constants.php'
 
	//function validating all the paramters are available
	//we will pass the required parameters to this function 
	function isTheseParametersAvailable($params){
		//assuming all parameters are available 
		$available = true; 
		$missingparams = ""; 
		
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		
		//if parameters are missing 
		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
			
			//displaying error
			echo json_encode($response);
			
			//stopping further execution
			die();
		}
	}
	
	//an array to display response
	$response = array();
	
	//if it is an api call 
	//that means a get parameter named api call is set in the URL 
	//and with this parameter we are concluding that it is an api call
	if(isset($_GET[PARAM_APICALL])){
		
		switch($_GET[PARAM_APICALL]){
			
			//the CREATE operation
			//if the api call value is 'createhero'
			//we will create a record in the database
			case API_CREATEUSER:
				//first check the parameters required for this request are available or not 
				isTheseParametersAvailable(array(PARAM_USERNAME,PARAM_PASSWORD,PARAM_ISADMIN));
				
				//creating a new dboperation object
				$db = new DbOperation();
				
				//creating a new record in the database
				$result = $db->createUser(
					$_POST[PARAM_USERNAME],
					$_POST[PARAM_PASSWORD],
					$_POST[PARAM_ISADMIN]
				);
				
 
				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 
 
					//in message we have a success message
					$response['message'] = 'User created successfully.';
 
					//and we are getting all the users from the database in the response
					$response['users'] = $db->getUsers();
				}else{
 
					//if record is not added that means there is an error 
					$response['error'] = true; 
 
					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}
				
			break; 
			
			//the READ operation
			//if the call is getheroes
			case API_GETUSERS:
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['users'] = $db->getUsers();
			break; 
			
			
			//the UPDATE operation
			case API_UPDATEUSER:
				isTheseParametersAvailable(array(PARAM_USERID,PARAM_USERNAME,PARAM_PASSWORD,PARAM_ISADMIN));
				$db = new DbOperation();
				$result = $db->updateUser(
					$_POST[PARAM_USERID],
					$_POST[PARAM_USERNAME],
					$_POST[PARAM_PASSWORD],
					$_POST[PARAM_ISADMIN]]
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'User updated successfully';
					$response['users'] = $db->getUsers();
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break; 
			
			//the delete operation
			case API_DELETEUSER:
 
				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET[PARAM_USERID])){
					$db = new DbOperation();
					if($db->deleteUser($_GET[PARAM_USERID])){
						$response['error'] = false; 
						$response['message'] = 'user deleted successfully';
						$response['users'] = $db->getUsers();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'UserID is required.';
				}
			break; 
			case API_LOGIN:
				isTheseParametersAvailable(array(PARAM_USERNAME,PARAM_PASSWORD));
				$db = new DbOperation();
				$response = $db->loginUser($_POST[PARAM_USERNAME],$_POST[PARAM_PASSWORD]);
			break;
			case API_GETUSER:
				if (isset($_GET[PARAM_USERID])){
					$db = new DbOperation();
					$response = $db->getUser($_GET[PARAM_USERID]);
				}else{
					$response['error'] = true;
					$response['message'] = "UserID is required;"
				}
			break;
		}
		
	}else{
		//if it is not api call 
		//pushing appropriate values to response array 
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	//displaying the response in json structure 
	echo json_encode($response);