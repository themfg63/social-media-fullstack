package com.TheMFG.backend.service;

import com.TheMFG.backend.dto.FindUsernameDTO;
import com.TheMFG.backend.dto.RegistrationObject;
import com.TheMFG.backend.exception.*;
import com.TheMFG.backend.model.Image;
import com.TheMFG.backend.model.Role;
import com.TheMFG.backend.model.User;
import com.TheMFG.backend.repository.RoleRepository;
import com.TheMFG.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository  roleRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public User registerUser(RegistrationObject dto){
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setDateOfBirth(dto.getDt());

        String name = user.getFirstName() + user.getLastName();
        boolean nameTaken = true;
        String tempName = "";
        while(nameTaken){
            tempName = generateUserName(name);
            if(userRepository.findByUsername(tempName).isEmpty()){
                nameTaken = false;
            }
        }

        user.setUsername(tempName);

        Set<Role> roles = user.getAuthorities();
        if(roleRepository.findByAuthority("USER").isPresent()){
            roles.add(roleRepository.findByAuthority("USER").get());
            user.setAuthorities(roles);
        }

        try{
            return userRepository.save(user);
        }catch (Exception e){
            throw new EmailAlreadyTakenException();
        }
    }

    private String generateUserName(String name){
        long generateNumber = (long) Math.floor(Math.random() * 1_000_000_000);
        return name + generateNumber;
    }

    public User getUserById(Integer userId){
        return userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
    }

    public User update(User user){
        try{
            return userRepository.save(user);
        }catch (Exception e){
            throw new EmailAlreadyTakenException();
        }
    }

    public void generateEmailVerification(String username){
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        user.setVerification(generateVerificationNumber());

        try{
            mailService.sendMail(user.getEmail(),"Doğrulama Kodu","Doğrulama Kodunuz: " + user.getVerification());
            userRepository.save(user);
        }catch (Exception e){
            throw new EmailFaildToSendException();
        }
        userRepository.save(user);
    }

    private Long generateVerificationNumber(){
        return (long) Math.floor(Math.random() * 100_000_000);
    }

    public User verifyEmail(String username,Long code){
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        if(code.equals(user.getVerification())){
            user.setEnabled(true);
            user.setVerification(null);
            return userRepository.save(user);
        }else{
            throw new IncorrectVerificationCodeException();
        }
    }

    public User setPassword(String username,String password){
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String  username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı Bulunamadı"));
        Set<GrantedAuthority> authorities = user.getAuthorities()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        return userDetails;
    }

    public User setProfileOrBannerPicture(String username, MultipartFile file, String prefix) throws UnableToSavePhotoException{
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        Image image = imageService.uploadImage(file,prefix);
        if(prefix.equals("pfp")){
            user.setProfilePhoto(image);
        }else{
            user.setBannerPhoto(image);
        }
        return userRepository.save(user);
    }

    public Set<User> followUser(String  user, String follower){
        User loggedInUser = userRepository.findByUsername(user).orElseThrow(UserDoesNotExistException::new);

        Set<User> followingList = loggedInUser.getFollowing();
        User followedUser = userRepository.findByUsername(follower).orElseThrow(UserDoesNotExistException::new);

        Set<User> followersList = followedUser.getFollowers();

        followingList.add(followedUser);
        loggedInUser.setFollowing(followingList);

        followersList.add(loggedInUser);
        followedUser.setFollowers(followersList);

        userRepository.save(loggedInUser);
        userRepository.save(followedUser);

        return loggedInUser.getFollowing();
    }

    public Set<User> retrieveFollowingList(String username){
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        return user.getFollowing();
    }

    public Set<User> retrieveFollowersList(String username){
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        return user.getFollowers();
    }

    public String verifyUsername(FindUsernameDTO dto){
        User user = userRepository.findByEmailOrPhoneOrUsername(
                dto.getEmail(), dto.getPhone(), dto.getUsername()
        ).orElseThrow(UserDoesNotExistException::new);
        return user.getUsername();
    }

    public User getUsersEmailAndPhone(FindUsernameDTO dto){
        return userRepository.findByEmailOrPhoneOrUsername(
                dto.getEmail(),dto.getPhone(),dto.getUsername()
        ).orElseThrow(UserDoesNotExistException::new);
    }
}
