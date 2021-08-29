package shop.sirius.global.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.sirius.global.security.common.hierarchy.SecurityRoleHierarchy;
import shop.sirius.global.security.domain.*;
import shop.sirius.global.security.jwt.metadata.UrlFilterInvocationSecurityMetadataSource;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Profile({"local", "test", "prod"})
@Component
@RequiredArgsConstructor
public class DataInit implements ApplicationRunner {
    private final InitService initService;

    private final UrlFilterInvocationSecurityMetadataSource metadataSource;

    private final SecurityRoleHierarchy securityRoleHierarchy;

    // WAS 가 시작될 때 호출된다.
    @PostConstruct
    public void init() {
        initService.init();
    }


    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        metadataSource.reload();
        securityRoleHierarchy.reload();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }

    @Component
    static class InitService {
        @Autowired
        private EntityManager entityManager;

        @Transactional
        public void init() {

            Role role_user = Role.builder().roleName("ROLE_USER").build();
            Role role_admin = Role.builder().roleName("ROLE_ADMIN").build();

            // 권한 계층을 설정했기 때문에 권한 계층 정보를 저장해야 합니다.
            RoleHierarchy admin_roleHierarchy = RoleHierarchy.builder()
                    .child("ROLE_ADMIN")
                    .build();

            RoleHierarchy user_roleHierarchy = RoleHierarchy.builder()
                    .child("ROLE_USER")
                    .parent(admin_roleHierarchy)
                    .build();


            entityManager.persist(role_user);
            entityManager.persist(role_admin);
            entityManager.persist(admin_roleHierarchy);
            entityManager.persist(user_roleHierarchy);

            // /** 경로 자원에 대한 권한
            Resources resources = Resources.builder().resourceName("/**").resourceType(ResourceType.URL).orderNum(99999L).build();
            resources.addResourcesRole(ResourcesRole.builder().role(role_admin).build());
            entityManager.persist(resources);
        }
    }
}
