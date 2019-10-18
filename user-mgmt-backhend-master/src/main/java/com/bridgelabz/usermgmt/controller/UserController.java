package com.bridgelabz.usermgmt.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermgmt.dto.AgeGroupDTO;
import com.bridgelabz.usermgmt.dto.CountryData;
import com.bridgelabz.usermgmt.dto.LoginDTO;
import com.bridgelabz.usermgmt.dto.ProfileDTO;
import com.bridgelabz.usermgmt.dto.UserDTO;
import com.bridgelabz.usermgmt.exception.UserException;
import com.bridgelabz.usermgmt.model.LoginDateHistory;
import com.bridgelabz.usermgmt.model.User;
import com.bridgelabz.usermgmt.repository.UserRepository;
import com.bridgelabz.usermgmt.service.IUserService;
import com.bridgelabz.usermgmt.util.Response;
import com.bridgelabz.usermgmt.util.ResponseToken;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/user")
@PropertySource("classpath:message.properties")
public class UserController {
	{
		System.out.println("UserController.enclosing_method()");
	}
	@Autowired
	IUserService userService;

	@Autowired
	UserRepository repository;

	/**
	 * @param userDto
	 * @return User
	 */
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody UserDTO userDto) {

		System.out.println("UserController.register()");
		System.out.println("UserDto:" + userDto);
		User response = userService.register(userDto);// onRegister(userDto);
		System.out.println(response);
		//Utility.send(userDto.getEmail(), "Activation mail", Utility.getUrl(response.getUser_id()));

		return new ResponseEntity<>(response, HttpStatus.OK);

	}
@GetMapping("/getfrom/rediscache")
	public ResponseEntity<List<User>> getUsersFromRedis(){
		List<User> user=userService.getUsersRedice();
		
		return new ResponseEntity(user,HttpStatus.OK);
	}
	
	
	
	/**
	 * @param loginDto
	 * @return ResponseToken
	 */
	@PostMapping("/login")
	public ResponseEntity<ResponseToken> login(@RequestBody LoginDTO loginDto) {

		ResponseToken statusResponse = userService.login(loginDto);
		return new ResponseEntity<ResponseToken>(statusResponse, HttpStatus.OK);
	}

	// to verify
	/**
	 * @param token
	 * @return
	 * @throws UserException
	 */
	@GetMapping(value = "/{token}/valid")
	public ResponseEntity<Response> emailValidation(@PathVariable String token) throws UserException {

		Response response = userService.validateEmailId(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	/**
	 * @param userDto
	 * @return User
	 */
	@PostMapping("/update")
	public ResponseEntity<User> update(@RequestBody UserDTO userDto) {
		User response = userService.register(userDto);// onRegister(userDto);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	/**
	 * @return List<UserDTO>
	 */
	@GetMapping("/list")
	public ResponseEntity<List<UserDTO>> getAllUser() {
		List<UserDTO> userList = userService.getAllUsers();// onRegister(userDto);

		return new ResponseEntity<>(userList, HttpStatus.OK);

	}

	@GetMapping("/get/profile")
	public ResponseEntity<ProfileDTO> getAllProfileByUserName(@RequestParam String userName) {
		ProfileDTO userList = userService.getUserProfile(userName);// onRegister(userDto);

		return new ResponseEntity<>(userList, HttpStatus.OK);

	}

	/**
	 * @param token
	 * @return UserDTO
	 */
	@PostMapping("/search")
	public ResponseEntity<UserDTO> searchUser(@RequestParam String token) {
		UserDTO user = userService.searchUser(token);// onRegister(userDto);

		return new ResponseEntity<UserDTO>(user, HttpStatus.OK);

	}

	@PostMapping("/uploadprofilepic")
	public ResponseEntity<Response> uploadProfilePic(@RequestHeader String token, @RequestParam MultipartFile file) {
		Response statusResponse = userService.uploadImage(token, file);
		return new ResponseEntity<Response>(statusResponse, HttpStatus.OK);
	}

	@PostMapping("/uploadprofilepic{userId}")
	public ResponseEntity<Response> uploadProfilePicByUserId(@PathVariable Long userId,
			@RequestParam MultipartFile file) {
		Response statusResponse = userService.uploadImageByUserId(userId, file);
		return new ResponseEntity<Response>(statusResponse, HttpStatus.OK);
	}

	@GetMapping("/get/loginsession{token}")
	public ResponseEntity<Set<LoginDateHistory>> addLogingHistory(@PathVariable String token) {
		Set<LoginDateHistory> resourseStatus = userService.getLoginHistory(token);
		return new ResponseEntity<>(resourseStatus, HttpStatus.OK);
	}

	@GetMapping("get/lastupdate/{token}")
	public ResponseEntity<LoginDateHistory> getLastUpdate(@PathVariable String token) {
		LoginDateHistory dateHistory = userService.getLastUpdated(token);
		return new ResponseEntity<>(dateHistory, HttpStatus.OK);

	}

	@GetMapping("/getuploadedimage/{token}")
	public Resource getProfilePic(@PathVariable String token) {
		Resource resourseStatus = userService.getUploadedImageOfUser(token);
		System.out.println(resourseStatus + "photo is");
		return resourseStatus;
	}

	@DeleteMapping("/delete{userId}")
	public ResponseEntity<Response> deleteUser(@PathVariable Long userId) {

		Response response = userService.deleteUser(userId);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/getuploadedimageByuserId/{userId}")
	public Resource getProfilePicByUserId(@PathVariable Long userId) {
		Resource resourseStatus = userService.getProfilePicByUserId(userId);
		System.out.println(resourseStatus + "photo is");
		return resourseStatus;
	}

	@GetMapping("search/user{userId}")
	public ResponseEntity<List<User>> searchUser(@PathVariable Long userId) {
		List<User> user = userService.searchUserByUserId(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("get/activecount")
	public ResponseEntity<Integer> getActiveCount() {

		Integer activeCount = userService.getActiveCount();
		return new ResponseEntity<Integer>(activeCount, HttpStatus.OK);
	}
	
	@GetMapping("get/malepercentage")
	public  ResponseEntity<Integer> getMalePercentage(){
		Integer genderPercentage=(Integer) userService.getMalePercentage();
	
		return new ResponseEntity<>(genderPercentage, HttpStatus.OK);

	}
	
	@GetMapping("get/countrydata")
	public ResponseEntity<CountryData>getCountryData(){
		CountryData countryData=userService.getCountryData();
		
		
		return new ResponseEntity<>(countryData,HttpStatus.OK);
	}
	
	
	@GetMapping("/get/age/group")
	public ResponseEntity<AgeGroupDTO> getAgeGroup(){
		
		AgeGroupDTO ageGroupDTO=userService.getAgeGroup();
		return new ResponseEntity<AgeGroupDTO>(ageGroupDTO,HttpStatus.OK);
	}
	
	
	
	
}
