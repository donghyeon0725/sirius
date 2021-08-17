package shop.sirius.global.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.sirius.global.security.domain.RoleHierarchy;
import shop.sirius.global.security.repository.RoleHierarchyRepository;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    /***
     * String 의 포멧이 맞아야 한다.
     *
     * */
    @Transactional
    public String findAllHierarchy() {

        List<RoleHierarchy> rolesHierarchy = roleHierarchyRepository.findAll();

        Iterator<RoleHierarchy> itr = rolesHierarchy.iterator();
        StringBuffer concatedRoles = new StringBuffer();
        while (itr.hasNext()) {
            RoleHierarchy model = itr.next();
            if (model.getParent() != null) {
                concatedRoles.append(model.getParent().getChild());
                concatedRoles.append(" > ");
                concatedRoles.append(model.getChild());
                concatedRoles.append("\n");
            }
        }
        return concatedRoles.toString();
    }
}
