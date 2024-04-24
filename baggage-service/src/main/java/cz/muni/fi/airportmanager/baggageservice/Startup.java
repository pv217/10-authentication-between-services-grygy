package cz.muni.fi.airportmanager.baggageservice;


import cz.muni.fi.airportmanager.baggageservice.entity.User;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle;
import io.smallrye.common.vertx.VertxContext;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import org.hibernate.reactive.mutiny.Mutiny;

@Singleton
public class Startup {


    void onStart(@Observes StartupEvent event, Vertx vertx, Mutiny.SessionFactory factory) {

        // We need a duplicated vertx context for hibernate reactive
        Context context = VertxContext.getOrCreateDuplicatedContext(vertx);
        // Don't forget to mark the context safe
        VertxContextSafetyToggle.setContextSafe(context, true);
        // Run the logic on the context created above
        context.runOnContext(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                // We cannot use the Panache.withTransaction() and friends because the CDI request context is not active/propagated

                factory.withTransaction(session ->
                                                User.deleteAll().onItem().transformToUni(ignored ->
                                User.add("passenger-service", "secret", "user")
                ))
                // We need to subscribe to the Uni to trigger the action
                        .subscribe().with(v -> {
                });
            }
        });
    }
}