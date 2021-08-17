package shop.sirius.global.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.sirius.global.security.domain.Role;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Profile("local")
@Component
@RequiredArgsConstructor
public class DataInit {
    private final InitService initService;

    // WAS 가 시작될 때 호출된다.
    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    static class InitService {
        @Autowired
        private EntityManager entityManager;

        @Transactional
        public void init() {
            entityManager.persist(Role.builder().roleName("ROLE_USER").build());
            entityManager.persist(Role.builder().roleName("ROLE_ADMIN").build());
        }
    }
}
