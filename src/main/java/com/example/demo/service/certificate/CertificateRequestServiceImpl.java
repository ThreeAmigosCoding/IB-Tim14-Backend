package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.model.user.Role;
import com.example.demo.model.user.User;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import com.example.demo.service.role.RoleService;
import com.example.demo.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CertificateRequestServiceImpl implements CertificateRequestService{

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CertificateRequestServiceImpl.class);

    @Override
    public CertificateRequest findById(Integer id) {
        return certificateRequestRepository.findById(id).orElseThrow();
    }

    @Override
    public CertificateRequestDTO rejectCertificateRequest(Integer userId, Integer requestId) throws Exception {
        logger.info("Rejecting certificate request for user {}, and request {}", userId, requestId);
        CertificateRequest request = this.validateRequestRejection(userId, requestId);

        request.setApproved(false);
        return new CertificateRequestDTO(certificateRequestRepository.save(request));
    }

    @Override
    public CertificateRequest validateRequestRejection(Integer userId, Integer requestId) throws Exception {
        logger.info("Validating request rejection for user {}, and request {}", userId, requestId);
        CertificateRequest request = this.certificateRequestRepository.findById(requestId)
                .orElseThrow(() -> new Exception("Request does not exist!"));
        Role admin = roleService.findByName("ROLE_ADMIN");
        User user = userService.findById(userId)
                .orElseThrow(() -> new Exception("User does not exist!"));
        if (user.getAuthorities().contains(admin))
            return request;
        if (request.getIssuer() != null && !Objects.equals(request.getIssuer().getOwner().getId(), userId))
            throw new Exception("You can not reject this request!");

        logger.info("Request rejection for user {} and request {} validated", userId, requestId);
        return request;
    }


    @Override
    public List<CertificateRequestDTO> getAllUserRequests(Integer id) {
        logger.info("Getting all requests for user {}", id);
        return certificateRequestRepository.findAllByOwnerId(id).stream()
                .map(CertificateRequestDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<CertificateRequestDTO> findAllRequests() {
        return certificateRequestRepository.findAll().stream()
                .map(CertificateRequestDTO::new).collect(Collectors.toList());
    }
}
