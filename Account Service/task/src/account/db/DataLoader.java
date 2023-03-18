package account.db;

import account.db.repository.PrivilegeRepository;
import account.db.repository.RoleRepository;
import account.db.model.Privilege;
import account.db.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;

        Privilege getPayment = createPrivilegeIfNotFound(Privilege.READ_PAYMENT);
        Privilege uploadPayment = createPrivilegeIfNotFound(Privilege.UPLOAD_PAYMENT);
        Privilege getUser = createPrivilegeIfNotFound(Privilege.READ_USER);
        Privilege deleteUser = createPrivilegeIfNotFound(Privilege.REMOVE_USER);
        Privilege manageRole = createPrivilegeIfNotFound(Privilege.MANAGE_ROLE);

        List<Privilege> userPrivileges = Collections.singletonList(getPayment);
        List<Privilege> accountantPrivileges = Arrays.asList(getPayment, uploadPayment);
        List<Privilege> adminPrivileges = Arrays.asList(getUser, deleteUser, manageRole);

        createRoleIfNotFound(Role.USER, userPrivileges);
        createRoleIfNotFound(Role.ACCOUNTANT, accountantPrivileges);
        createRoleIfNotFound(Role.ADMIN, adminPrivileges);
        createRoleIfNotFound(Role.AUDITOR, adminPrivileges);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}