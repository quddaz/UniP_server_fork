package unip.universityInParty.domain.pmList.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPMList is a Querydsl query type for PMList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPMList extends EntityPathBase<PMList> {

    private static final long serialVersionUID = -1453894124L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPMList pMList = new QPMList("pMList");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final unip.universityInParty.domain.member.entity.QMember member;

    public final unip.universityInParty.domain.party.entity.QParty party;

    public final EnumPath<unip.universityInParty.domain.pmList.entity.Enum.PartyRole> role = createEnum("role", unip.universityInParty.domain.pmList.entity.Enum.PartyRole.class);

    public QPMList(String variable) {
        this(PMList.class, forVariable(variable), INITS);
    }

    public QPMList(Path<? extends PMList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPMList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPMList(PathMetadata metadata, PathInits inits) {
        this(PMList.class, metadata, inits);
    }

    public QPMList(Class<? extends PMList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new unip.universityInParty.domain.member.entity.QMember(forProperty("member")) : null;
        this.party = inits.isInitialized("party") ? new unip.universityInParty.domain.party.entity.QParty(forProperty("party"), inits.get("party")) : null;
    }

}

