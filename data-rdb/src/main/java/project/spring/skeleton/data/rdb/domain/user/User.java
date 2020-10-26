package project.spring.skeleton.data.rdb.domain.user;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import project.spring.skeleton.data.rdb.config.encrypt.converter.StringAES256CryptConverter;
import project.spring.skeleton.data.rdb.domain.BaseTimeEntity;

import javax.persistence.*;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Getter
@Entity
@DynamicUpdate
@Table(name = "users")
@AllArgsConstructor
@Audited(targetAuditMode = NOT_AUDITED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    private String name;
    private String email;

    @Convert(converter = StringAES256CryptConverter.class)
    private String phoneNumber;
}