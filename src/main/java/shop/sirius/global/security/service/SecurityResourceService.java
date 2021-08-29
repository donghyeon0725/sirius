package shop.sirius.global.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import shop.sirius.global.error.ErrorCode;
import shop.sirius.global.error.exception.ResourceNotFoundException;
import shop.sirius.global.security.domain.Resources;
import shop.sirius.global.security.domain.Role;
import shop.sirius.global.security.repository.AccessIpRepository;
import shop.sirius.global.security.repository.ResourcesRepository;
import shop.sirius.global.security.repository.RoleRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityResourceService {
    private final ResourcesRepository resourcesRepository;

    private final RoleRepository roleRepository;

    private final AccessIpRepository accessIpRepository;


    @Cacheable(value = "resourceList")
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllResources();

        getResourceMap(result, resourcesList);
        return result;
    }

    private void getResourceMap(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result, List<Resources> resourcesList) {
        resourcesList.forEach(re ->
                {
                    List<ConfigAttribute> configAttributeList = new ArrayList<>();
                    re.getResourcesRoles().forEach(ro -> {
                        configAttributeList.add(new SecurityConfig(ro.getRole().getRoleName()));
                    });
                    result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributeList);
                }
        );
        log.debug("cache test");
    }

    private void getResourceStringMap(LinkedHashMap<String, List<ConfigAttribute>> result, List<Resources> resourcesList) {
        resourcesList.forEach(re ->
                {
                    List<ConfigAttribute> configAttributeList = new ArrayList<>();
                    re.getResourcesRoles().forEach(ro -> {
                        configAttributeList.add(new SecurityConfig(ro.getRole().getRoleName()));
                    });
                    result.put(re.getResourceName(), configAttributeList);
                }
        );
        log.debug("cache test");
    }

    @Cacheable(value = "methodResourceList")
    public LinkedHashMap<String, List<ConfigAttribute>> getMethodResourceList() {

        LinkedHashMap<String, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllMethodResources();

        getResourceStringMap(result, resourcesList);
        return result;
    }

    @Cacheable(value = "pointcutResourceList")
    public LinkedHashMap<String, List<ConfigAttribute>> getPointcutResourceList() {

        LinkedHashMap<String, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllPointcutResources();

        getResourceStringMap(result, resourcesList);
        return result;
    }

    public List<Resources> getResources() {
        return resourcesRepository.findAllResources();
    }

    public Resources getResource(Long id) {
        return resourcesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    /**
     * 모든 ip 리스트를 가져온다.
     * */
    @Cacheable(value = "accessIpList")
    public List<String> getAccessIpList() {

        List<String> accessIpList = accessIpRepository.findAll().stream().map(accessIp -> accessIp.getIpAddress()).collect(Collectors.toList());

        return accessIpList;
    }
}

