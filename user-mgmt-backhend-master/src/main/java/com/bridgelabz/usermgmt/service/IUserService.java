package com.bridgelabz.usermgmt.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermgmt.dto.AgeGroupDTO;
import com.bridgelabz.usermgmt.dto.CountryData;
import com.bridgelabz.usermgmt.dto.LoginDTO;
import com.bridgelabz.usermgmt.dto.ProfileDTO;
import com.bridgelabz.usermgmt.dto.UserDTO;
import com.bridgelabz.usermgmt.exception.UserException;
import com.bridgelabz.usermgmt.model.LoginDateHistory;
import com.bridgelabz.usermgmt.model.User;
import com.bridgelabz.usermgmt.util.Response;
import com.bridgelabz.usermgmt.util.ResponseToken;

public interface IUserService {
	User register(UserDTO user);

	/**
	 * @param token
	 * @return
	 * @throws UserException
	 */
	Response validateEmailId(String token) throws UserException;

	ResponseToken login(LoginDTO loginDTO);

	Response update(UserDTO user);

	List<UserDTO> getAllUsers();

	UserDTO searchUser(String token);

	Response uploadImage(String token, MultipartFile file);

	public Resource getUploadedImage(String token);

	public Set<LoginDateHistory> getLoginHistory(String token);

	LoginDateHistory getLastUpdated(String token);

	List<ProfileDTO> getAllProfiles();

//	Resource getUploadedImage(Long id);

	ResponseToken authentication(Optional<User> user, String password);

	ProfileDTO getUserProfile(String username);

	Resource getUploadedImageOfUser(String token);

	Response deleteUser(Long userId);

	Response uploadImageByUserId(Long userId, MultipartFile file);

	Resource getProfilePicByUserId(Long userId);

	List<User> searchUserByUserId(Long userId);

	Integer getActiveCount();

	Integer getMalePercentage();

	CountryData getCountryData();

	AgeGroupDTO getAgeGroup();

	List<User> getUsersRedice();


}
