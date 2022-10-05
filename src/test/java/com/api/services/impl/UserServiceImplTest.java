package com.api.services.impl;

import com.api.domain.User;
import com.api.domain.dto.UserDTO;
import com.api.repositories.UserRepository;
import com.api.services.exceptions.DataIntegratyViolationException;
import com.api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
class UserServiceImplTest {

    public static final int ID = 1;
    public static final String NAME = "Valdir";
    public static final String EMAIL = "valdir@email.com";
    public static final String PASSWORD = "123";
    public static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado.";
    public static final int INDEX = 0;
    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper mapper;

    private User user;

    private UserDTO userDTO;

    private Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenFindByIdThenReturnAnUserInstance() {
        Mockito.when(repository.findById(anyInt())).thenReturn(optionalUser);

        User response = service.findById(ID);

        assertNotNull(response);
        Assertions.assertEquals(User.class, response.getClass());
        Assertions.assertEquals(ID, response.getId());
        Assertions.assertEquals(NAME, response.getName());
        Assertions.assertEquals(EMAIL, response.getEmail());
    }

    @Test
    void whenFindByIdThenReturnAnObjectNotFoundException(){
        Mockito.when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));

        try{
            service.findById(ID);
        } catch (Exception e){
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJETO_NAO_ENCONTRADO, e.getMessage());
        }
    }

    @Test
    void whenFindAllThenReturnAnListOf() {
        Mockito.when(repository.findAll()).thenReturn(List.of(user));

        List<User> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(User.class, response.get(0).getClass());
        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NAME, response.get(INDEX).getName());
        assertEquals(EMAIL, response.get(INDEX).getEmail());

    }

    @Test
    void whenCreateAUserThenReturnSuccess() {
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);

        User response = service.create(userDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    void whenCreateThenReturnAnDataIntegrityViolationException() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(optionalUser);

        try{
            optionalUser.get().setId(2);
            service.create(userDTO);
        } catch (Exception e) {
            assertEquals(DataIntegratyViolationException.class, e.getClass());
            assertEquals("E-mail já cadastrado no sistema", e.getMessage());
        }

    }

    @Test
    void whenUpdateAUserThenReturnSuccess() {
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);

        User response = service.update(userDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    void whenUpdateThenReturnAnDataIntegrityViolationException() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(optionalUser);

        try{
            optionalUser.get().setId(2);
            service.update(userDTO);
        } catch (Exception e) {
            assertEquals(DataIntegratyViolationException.class, e.getClass());
            assertEquals("E-mail já cadastrado no sistema", e.getMessage());
        }

    }

    @Test
    void deleteWithSuccess() {
        Mockito.when(repository.findById(anyInt())).thenReturn(optionalUser);
        Mockito.doNothing().when(repository).deleteById(anyInt());

        service.delete(ID);

        Mockito.verify(repository, Mockito.times(1)).deleteById(anyInt());
    }

    private void startUser(){
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
        optionalUser = Optional.of( new User(ID, NAME, EMAIL, PASSWORD));

    }
}