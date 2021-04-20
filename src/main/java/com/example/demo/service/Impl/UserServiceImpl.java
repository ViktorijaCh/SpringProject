package com.example.demo.service.Impl;

import com.example.demo.exc.UserAlreadyExistsException;
import com.example.demo.exc.UserNotFoundException;
import com.example.demo.model.ArtPrint;
import com.example.demo.model.User;
import com.example.demo.repository.ArtPrintRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ArtPrintRepository artPrintRepository;

    public UserServiceImpl(UserRepository userRepository, ArtPrintRepository artPrintRepository) {
        this.userRepository = userRepository;
        this.artPrintRepository = artPrintRepository;
    }

    @Override
    public User findById(String username) {
        return this.userRepository.findById(username).orElseThrow(()->new UserNotFoundException(username));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findById(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));

    }
    @Override
    public User registerUser(User user) {
        if (this.userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        return this.userRepository.save(user);
    }

    @Override
    public boolean existsById(String id) {
        return this.userRepository.existsById(id);
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void like(Long id, User user) {
        ArtPrint artPrint = this.artPrintRepository.findById(id).orElseThrow(()->new NoSuchElementException());
        artPrint.setLikes(artPrint.getLikes()+1);
        List<ArtPrint> newli=new ArrayList<>();
        List<User> pom=new ArrayList<>();
        List<User> users=this.userRepository.findUsersByLikedartprintsIsContaining(artPrint);
        List<ArtPrint>liked= artPrintRepository.findArtPrintsByUsersLikedContaining(user);
        if (liked.size()==0) {
            newli.add(artPrint);
            user.setLikedartprints(newli);
        }
        else {
            liked.add(artPrint);
            user.setLikedartprints(liked);
        }
        if (users.size()==0) {
            pom.add(user);
            artPrint.setUsersLiked(pom);
        }
        else {
            users.add(user);
            artPrint.setUsersLiked(users);
        }
        artPrintRepository.save(artPrint);
        userRepository.save(user);
    }

}
