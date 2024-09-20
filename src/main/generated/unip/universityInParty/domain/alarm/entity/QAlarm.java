package unip.universityInParty.domain.alarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarm is a Querydsl query type for Alarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarm extends EntityPathBase<Alarm> {

    private static final long serialVersionUID = 1100153604L;

    public static final QAlarm alarm = new QAlarm("alarm");

    public final EnumPath<unip.universityInParty.domain.alarm.entity.Enum.AlarmCategory> alarmCategory = createEnum("alarmCategory", unip.universityInParty.domain.alarm.entity.Enum.AlarmCategory.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> receiver = createNumber("receiver", Long.class);

    public final NumberPath<Long> sender = createNumber("sender", Long.class);

    public final DateTimePath<java.time.LocalDateTime> time = createDateTime("time", java.time.LocalDateTime.class);

    public QAlarm(String variable) {
        super(Alarm.class, forVariable(variable));
    }

    public QAlarm(Path<? extends Alarm> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarm(PathMetadata metadata) {
        super(Alarm.class, metadata);
    }

}

