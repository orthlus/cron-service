package main.payments.reminders;

import art.aelaort.telegram.entity.RemindToSend;
import lombok.RequiredArgsConstructor;
import main.tables.PaymentsHoldsOnReminders;
import main.tables.PaymentsReminders;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static main.Tables.PAYMENTS_HOLDS_ON_REMINDERS;
import static main.tables.PaymentsReminders.PAYMENTS_REMINDERS;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.extract;

@Component
@RequiredArgsConstructor
public class Repo {
	private final DSLContext db;
	private final PaymentsHoldsOnReminders phor = PAYMENTS_HOLDS_ON_REMINDERS;
	private final PaymentsReminders pr = PAYMENTS_REMINDERS;

	public List<RemindToSend> getRemindsForNow() {
		LocalDateTime now = LocalDateTime.now();

		Condition startDayEq = pr.START_DAY_OF_MONTH.lessOrEqual(extract(now, DatePart.DAY));
		Condition endDayEq = pr.END_DAY_OF_MONTH.greaterOrEqual(extract(now, DatePart.DAY));
		Condition timeEq = pr.HOUR_OF_DAY_TO_FIRE.eq(extract(now, DatePart.HOUR));
		Condition holdDateEq = phor.HOLD_END_DT.lessOrEqual(now.toLocalDate());

		return db.select(pr.ID, pr.REMIND_NAME)
				.from(pr)
				.leftJoin(phor)
				.on(pr.ID.eq(phor.REMIND_ID))
				.where(startDayEq
						.and(endDayEq)
						.and(timeEq)
						.and(coalesce(holdDateEq, true)))
				.fetch(mapping(RemindToSend::new));
	}
}
