package unip.universityInParty.domain.alarmDetail.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarmDetail is a Querydsl query type for AlarmDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmDetail extends EntityPathBase<AlarmDetail> {

    private static final long serialVersionUID = 1159560292L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarmDetail alarmDetail = new QAlarmDetail("alarmDetail");

    public final unip.universityInParty.domain.alarm.entity.QAlarm alarm;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> party = createNumber("party", Long.class);

    public QAlarmDetail(String variable) {
        this(AlarmDetail.class, forVariable(variable), INITS);
    }

    public QAlarmDetail(Path<? extends AlarmDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarmDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarmDetail(PathMetadata metadata, PathInits inits) {
        this(AlarmDetail.class, metadata, inits);
    }

    public QAlarmDetail(Class<? extends AlarmDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alarm = inits.isInitialized("alarm") ? new unip.universityInParty.domain.alarm.entity.QAlarm(forProperty("alarm")) : null;
    }

}

