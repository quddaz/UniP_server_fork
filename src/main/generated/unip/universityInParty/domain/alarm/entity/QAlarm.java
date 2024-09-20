package unip.universityInParty.domain.alarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarm is a Querydsl query type for Alarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarm extends EntityPathBase<Alarm> {

    private static final long serialVersionUID = 1100153604L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarm alarm = new QAlarm("alarm");

    public final EnumPath<unip.universityInParty.domain.alarm.entity.Enum.AlarmCategory> alarmCategory = createEnum("alarmCategory", unip.universityInParty.domain.alarm.entity.Enum.AlarmCategory.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final unip.universityInParty.domain.member.entity.QMember receiver;

    public final unip.universityInParty.domain.member.entity.QMember sender;

    public final DateTimePath<java.time.LocalDateTime> time = createDateTime("time", java.time.LocalDateTime.class);

    public QAlarm(String variable) {
        this(Alarm.class, forVariable(variable), INITS);
    }

    public QAlarm(Path<? extends Alarm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarm(PathMetadata metadata, PathInits inits) {
        this(Alarm.class, metadata, inits);
    }

    public QAlarm(Class<? extends Alarm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new unip.universityInParty.domain.member.entity.QMember(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new unip.universityInParty.domain.member.entity.QMember(forProperty("sender")) : null;
    }

}

