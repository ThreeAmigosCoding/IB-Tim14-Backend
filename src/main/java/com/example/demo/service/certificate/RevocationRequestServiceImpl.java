package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.dto.certificate.RevocationRequestDto;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.RevocationRequest;
import com.example.demo.model.user.User;
import com.example.demo.repository.certificate.CertificateRepository;
import com.example.demo.repository.certificate.RevocationRequestRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.role.RoleService;
import com.example.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RevocationRequestServiceImpl implements RevocationRequestService {

    @Autowired
    private RevocationRequestRepository revocationRequestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @Override
    public RevocationRequest findById(Integer id) throws Exception {
        return revocationRequestRepository.findById(id).orElseThrow(() -> new Exception("Revocation request " +
                "doesn't exist!"));
    }

    @Override
    public RevocationRequestDto rejectRevocationRequest(Integer userId, Integer requestId) throws Exception {
        RevocationRequest request = findById(requestId);

        validateRevocation(userId, request.getIssuer());

        request.setApproved(false);
        return new RevocationRequestDto(revocationRequestRepository.save(request));
    }

    @Override
    public List<RevocationRequestDto> getAllUserRequests(Integer userId) {
        return revocationRequestRepository.findAllByUserId(userId).stream().
                map(RevocationRequestDto::new).collect(Collectors.toList());
    }

    private void validateRevocation(Integer userId, Certificate certificate) throws Exception {
        User user = userService.findById(userId).orElseThrow(() -> new Exception("User doesn't exist!"));
        if (!user.getAuthorities().contains(roleService.findByName("ROLE_ADMIN")) &&
                !Objects.equals(userId, certificate.getOwner().getId()))
            throw new Exception("You are not allowed to reject this revocation request!");
    }

}
