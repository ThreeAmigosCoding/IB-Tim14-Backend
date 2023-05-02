package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.model.user.Role;
import com.example.demo.model.user.User;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import com.example.demo.service.role.RoleService;
import com.example.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateRequestServiceImpl implements CertificateRequestService{

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Override
    public CertificateRequest findById(Integer id) {
        return certificateRequestRepository.findById(id).orElseThrow();
    }

    @Override
    public CertificateRequestDTO rejectCertificateRequest(Integer userId, Integer requestId) throws Exception {
        CertificateRequest request = this.validateRequestRejection(userId, requestId);

        request.setApproved(false);
        return new CertificateRequestDTO(certificateRequestRepository.save(request));
    }

    @Override
    public CertificateRequest validateRequestRejection(Integer userId, Integer requestId) throws Exception {
        CertificateRequest request = this.certificateRequestRepository.findById(requestId)
                .orElseThrow(() -> new Exception("Request does not exist!"));
        Role admin = roleService.findByName("ROLE_ADMIN");
        User user = userService.findById(userId)
                .orElseThrow(() -> new Exception("User does not exist!"));
        if (!user.getAuthorities().contains(admin) || !Objects.equals(request.getIssuer().getOwner().getId(), userId))
            throw new Exception("You can not reject this request!");

        return request;
    }


    @Override
    public List<CertificateRequestDTO> getAllUserRequests(Integer id) {
        return certificateRequestRepository.findAllByOwnerId(id).stream()
                .map(CertificateRequestDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<CertificateRequestDTO> findAllRequests() {
        return certificateRequestRepository.findAll().stream()
                .map(CertificateRequestDTO::new).collect(Collectors.toList());
    }
}
