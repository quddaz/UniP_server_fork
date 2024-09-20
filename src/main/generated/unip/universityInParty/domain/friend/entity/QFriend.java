package unip.universityInParty.domain.friend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriend is a Querydsl query type for Friend
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriend extends EntityPathBase<Friend> {

    private static final long serialVersionUID = -1901192774L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriend friend = new QFriend("friend");

    public final unip.universityInParty.domain.member.entity.QMember fromMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final unip.universityInParty.domain.member.entity.QMember toMember;

    public QFriend(String variable) {
        this(Friend.class, forVariable(variable), INITS);
    }

    public QFriend(Path<? extends Friend> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriend(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriend(PathMetadata metadata, PathInits inits) {
        this(Friend.class, metadata, inits);
    }

    public QFriend(Class<? extends Friend> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromMember = inits.isInitialized("fromMember") ? new unip.universityInParty.domain.member.entity.QMember(forProperty("fromMember")) : null;
        this.toMember = inits.isInitialized("toMember") ? new unip.universityInParty.domain.member.entity.QMember(forProperty("toMember")) : null;
    }

}

