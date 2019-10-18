package com.bridgelabz.usermgmt.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermgmt.dto.AgeGroupDTO;
import com.bridgelabz.usermgmt.dto.CountryData;
import com.bridgelabz.usermgmt.dto.LoginDTO;
import com.bridgelabz.usermgmt.dto.ProfileDTO;
import com.bridgelabz.usermgmt.dto.UserDTO;
import com.bridgelabz.usermgmt.exception.UserException;
import com.bridgelabz.usermgmt.model.LoginDateHistory;
import com.bridgelabz.usermgmt.model.User;
import com.bridgelabz.usermgmt.repository.LoginRepository;
import com.bridgelabz.usermgmt.repository.RedisUserRepository;
import com.bridgelabz.usermgmt.repository.UserRepository;
import com.bridgelabz.usermgmt.util.Response;
import com.bridgelabz.usermgmt.util.ResponseToken;
import com.bridgelabz.usermgmt.util.StatusHelper;
import com.bridgelabz.usermgmt.util.TokenUtil;

@Service("userService")
public class UserService implements IUserService {

	@Autowired
	ModelMapper mapper;

	@Autowired
	UserRepository userRepository;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	TokenUtil tokenUtil;
	
	@Autowired
	RedisUserRepository redisRepo;

	@PersistenceContext
	private EntityManager entityManager;

	private final Path fileLocation = Paths.get("/home/admin111/Desktop/MyFiles");

	@Override
	public User register(UserDTO userDTO) {
		Optional<User> isAvailable = userRepository.findByUsername(userDTO.getUsername());
		if (isAvailable.isPresent()) {
			throw new UserException(404, "User already exists");
		}
		User user = mapper.map(userDTO, User.class);

		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		redisRepo.save(user);
		System.out.println("stored in Redis");
		userRepository.save(user);

		return user;
	}

	@Override
	public ResponseToken login(LoginDTO loginDto) {
		// extract user details by using emailid
		Optional<User> user = userRepository.findByUsername(loginDto.getUserName());
		System.out.println(user);
		if (user.isPresent()) {
			System.out.println("password..." + (loginDto.getPassword()));
			user.get().setActive(true);
			userRepository.save(user.get());
			return authentication(user, loginDto.getPassword());

		}
		return null;

	}

	@Override
	public ResponseToken authentication(Optional<User> user, String password) {

		ResponseToken response = new ResponseToken();

		boolean status = passwordEncoder.matches(password, user.get().getPassword());
		if (status == true) {
			System.out.println("logged in");
			String token = tokenUtil.createToken(user.get().getUser_id());
			LoginDateHistory dateHistory = new LoginDateHistory();
			dateHistory.setUser_id(user.get().getUser_id());
			dateHistory.setLogin_datetime_history(LocalDateTime.now());
			user.get().getLoginHistory().add(dateHistory);
			userRepository.save(user.get());
			response.setToken(token);
			response.setStatusCode(200);
			response.setEmail(user.get().getEmail());
			response.setProfilePic(user.get().getProfilePic());
			response.setUserName(user.get().getUsername());
			;
			response.setStatusMessage("user.login");
			return response;
		} else {
			return null;
		}

	}

	@Override
	public Response validateEmailId(String token) {
		Long id = tokenUtil.decodeToken(token);
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(404, "user.activation.fail"));
		user.setActive(true);
		userRepository.save(user);
		Response response = StatusHelper.statusResponse(200, "user.activation sucessful");
		return response;
	}

	@Override
	public Response update(UserDTO userDTO) {
		User user = mapper.map(userDTO, User.class);
		userRepository.save(user);
		Response response = StatusHelper.statusResponse(200, "updated successfully");

		return response;
	}

	@Override
	public List<UserDTO> getAllUsers() {

		List<User> allUserList = userRepository.findAll();
		List<UserDTO> allUserDTO = new ArrayList<UserDTO>();
		for (User allData : allUserList) {

			allUserDTO.add(mapper.map(allData, UserDTO.class));

		}

		// TODO Auto-generated method stub
		return allUserDTO;
	}

	@Override
	public UserDTO searchUser(String token) {
		Long userId = tokenUtil.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);

		UserDTO userDTO = mapper.map(user.get(), UserDTO.class);

		return userDTO;

	}

	@Override
	public Response uploadImage(String token, MultipartFile file) {
		long id = tokenUtil.decodeToken(token);// (token);
		Optional<User> user = userRepository.findById(id);

		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		try {
			Files.copy(file.getInputStream(), fileLocation.resolve(uniqueId), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

		}
		user.get().setProfilePic(uniqueId);
		userRepository.save(user.get());
		Response response = StatusHelper.statusResponse(200, "Image Uploaded Successfully");
		return response;
	}

	@Override
	public Resource getUploadedImage(String token) {
		long id = tokenUtil.decodeToken(token);
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserException(-5, "User does not exists");
		}
		Path imagePath = fileLocation.resolve(user.get().getProfilePic());
		try {
			Resource resource = new UrlResource(((java.nio.file.Path) imagePath).toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Set<LoginDateHistory> getLoginHistory(String token) {
		Long id = tokenUtil.decodeToken(token);

		Optional<User> user = userRepository.findById(id);
		Set<LoginDateHistory> dateHistories = user.get().getLoginHistory();
		System.out.println(user.get().getLoginHistory());
		return dateHistories;
	}

	@Override
	public LoginDateHistory getLastUpdated(String token) {
		Long id = tokenUtil.decodeToken(token);

		Optional<User> user = userRepository.findById(id);
		Set<LoginDateHistory> dateHistories = user.get().getLoginHistory();
		LoginDateHistory lastUpdate = new LoginDateHistory();
		if (user.isPresent()) {
			for (LoginDateHistory lastValue : dateHistories) {
				lastUpdate = lastValue;
			}
		}
		return lastUpdate;
	}

	@Override
	public List<ProfileDTO> getAllProfiles() {
		List<User> allUserList = userRepository.findAll();
		List<ProfileDTO> allUserDTO = new ArrayList<ProfileDTO>();
		for (User allData : allUserList) {

			allUserDTO.add(mapper.map(allData, ProfileDTO.class));

		}

		// TODO Auto-generated method stub
		return allUserDTO;
	}

	@Override
	public ProfileDTO getUserProfile(String username) {
		Optional<User> isAvailable = userRepository.findByUsername(username);
		if (isAvailable.isPresent()) {

			ProfileDTO profileDTO = mapper.map(isAvailable.get(), ProfileDTO.class);

			return profileDTO;

		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getUploadedImageOfUser(String token) {
		long userId = tokenUtil.decodeToken(token);

		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException(-5, "user already exist");
		}

		try {
			Path imageFile = fileLocation.resolve(user.get().getProfilePic());

			Resource resource = new UrlResource(imageFile.toUri());

			if (resource.exists() || (resource.isReadable())) {
				System.out.println(resource);
				return resource;
			} else {
				throw new Exception("Couldn't read file: " + imageFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Response deleteUser(Long userId) {

		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException(404, "User Does not exists");
		} else {
			userRepository.delete(user.get());
			Response response = StatusHelper.statusResponse(200, "User deleted Successfully");
			return response;
		}

	}

	@Override
	public Response uploadImageByUserId(Long userId, MultipartFile file) {
		Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent()) {
			throw new UserException(404, "User does not exists");
		}
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		try {
			Files.copy(file.getInputStream(), fileLocation.resolve(uniqueId), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

		}
		user.get().setProfilePic(uniqueId);
		userRepository.save(user.get());
		Response response = StatusHelper.statusResponse(200, "Image Uploaded Successfully");
		return response;

	}

	@Override
	public Resource getProfilePicByUserId(Long userId) {
//		long id = tokenUtil.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException(-5, "User does not exists");
		}
		Path imagePath = fileLocation.resolve(user.get().getProfilePic());
		try {
			Resource resource = new UrlResource(((java.nio.file.Path) imagePath).toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<User> searchUserByUserId(Long userId) {

		StoredProcedureQuery findByYearProcedure = entityManager.createNamedStoredProcedureQuery("search")
				.setParameter("userId", userId);
		// TODO Auto-generated method stub
		return findByYearProcedure.getResultList();
	}

	@Override
	public Integer getActiveCount() {

		List<User> allUsers = userRepository.findAll();
		List<User> activeUsers = allUsers.stream().filter(data -> data.isActive() == true).collect(Collectors.toList());

		
		return activeUsers.size();
	}

	@Override
	public Integer getMalePercentage() {
		int all, female;
		List<User> allUsers = userRepository.findAll();
		all = allUsers.size();
		System.out.println("UserService.getMalePercentage()::All Users=====>" + allUsers.size());
		List<User> allFeMale = allUsers.stream().filter(data -> data.gender.equalsIgnoreCase("Female"))
				.collect(Collectors.toList());
		female = allFeMale.size();
		System.out.println("UserService.getMalePercentage()::Female Users=====>" + allFeMale.size());

		System.out.println("Difference::" + (allUsers.size() - allFeMale.size()));
		Integer femalePercentage = (female * 100) / all;
		System.out.println("Percentage:" + femalePercentage);
		// TODO Auto-generated method stub
		return femalePercentage;
	}

	@Override
	public CountryData getCountryData() {
		CountryData countryData = new CountryData();
		Long ind, pak, bang, others;
		List<User> userList = userRepository.findAll();

		ind = userList.stream().filter(data -> data.getCountry().equalsIgnoreCase("India")).count();
		pak = userList.stream().filter(data -> data.getCountry().equalsIgnoreCase("Pakistan")).count();
		bang = userList.stream().filter(data -> data.getCountry().equalsIgnoreCase("Bangladesh")).count();
		others = userList.stream()
				.filter(data -> !data.getCountry().equalsIgnoreCase("India")
						|| !data.getCountry().equalsIgnoreCase("Pakistan")
						|| !data.getCountry().equalsIgnoreCase("Bangladesh"))
				.count();

		System.out.println("india" + ind);
		System.out.println("pak" + pak);
		System.out.println("ban" + bang);
		System.out.println("others" + others);

		countryData.setIndia(ind);
		countryData.setPakistan(pak);
		countryData.setBangladesh(bang);
		countryData.setOthers(others);

		return countryData;
	}

	@Override
	public AgeGroupDTO getAgeGroup() {
		AgeGroupDTO ageGroupDTO = new AgeGroupDTO();
		Integer age = 0, g1 = 0, g2 = 0, g3 = 0, g4 = 0, g5 = 0, g6 = 0,g7=0;
		List<User> userList = userRepository.findAll();
		for (User user : userList) {
			age = user.getAge();
			
			
			 if (age >= 18 && age <= 22) {
				ageGroupDTO.setGroup1(g1++);
			}
			else if(age >= 23 && age <= 27) {
				ageGroupDTO.setGroup2(g2++);

			}
			else if(age >= 28 && age <= 32) {
				ageGroupDTO.setGroup3(g3++);

			}
			else if(age >= 33 && age <= 37) {
				ageGroupDTO.setGroup4(g4++);

			}
			else if(age >= 38 && age <= 42) {
				ageGroupDTO.setGroup5(g5++);

			}
			else if(age>42) {
				ageGroupDTO.setGroup6(g6++);

			}
			else if(age <18) {
				ageGroupDTO.setGroup7(g7++);

			}
			
			
		}
		System.out.println(ageGroupDTO.toString());

		return ageGroupDTO;
	}

	@Override
	public List<User> getUsersRedice() {
		
		List<User> users = redisRepo.findAll();
		// TODO Auto-generated method stub
		return users;
	}

}
