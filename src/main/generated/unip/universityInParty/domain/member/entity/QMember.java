package unip.universityInParty.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1713803086L;

    public static final QMember member = new QMember("member1");

    public final BooleanPath auth = createBoolean("auth");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final StringPath profile_image = createString("profile_image");

    public final EnumPath<unip.universityInParty.domain.member.entity.Enum.Role> role = createEnum("role", unip.universityInParty.domain.member.entity.Enum.Role.class);

    public final EnumPath<unip.universityInParty.domain.member.entity.Enum.Status> status = createEnum("status", unip.universityInParty.domain.member.entity.Enum.Status.class);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

