package stanislav.tun.novinomad.picasso.exceptions;

import stanislav.tun.novinomad.picasso.persistance.pojos.*;

import javax.xml.bind.ValidationException;

public class OverlapsException extends Exception {
    private AbstractEntity entity;
    private Tour tour;
    private Tour overlapsTour;
    private DateTimeRange overlapsRange;

    public OverlapsException(AbstractEntity entity, Tour tour, Tour overlapsTour) {
        this.entity = entity;
        this.tour = tour;
        this.overlapsTour = overlapsTour;
        try {
            overlapsRange = new DateTimeRange(tour.getStartDate(), overlapsTour.getEndDate());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMessage() {
        return assembleMessage();
    }

    private String assembleMessage() {
        String result = null;
        if (entity instanceof Driver) {
            Driver d = (Driver) entity;

            result = String.format("Can't add participant %s to \ntour %s \nfor date %s \ncause overlaps with \ntour %s. \nOverlaps range %s",
                    d.getFullName(),
                    tour.getTittle(),
                    tour.getStartDate() + " - " + tour.getEndDate(),
                    overlapsTour.getTittle() + " " + overlapsTour.getStartDate() + " - " + overlapsTour.getEndDate(),
                    overlapsRange.toString());

        } else if (entity instanceof Guide) {
            Guide d = (Guide) entity;
            try {
                result = String.format("Can't add participant %s to tour %s for date %s cause overlaps with tour %s. Overlaps range %s",
                        d.getFullName(),
                        tour.getTittle(),
                        tour.getStartDate() + " - " + tour.getEndDate(),
                        overlapsTour.getTittle() + " " + tour.getStartDate() + " - " + tour.getEndDate(),
                        new DateTimeRange(overlapsTour.getStartDate(), tour.getEndDate()).toString());
            } catch (ValidationException e) {
                // ignored
            }
        }
        return result;
    }

    public AbstractEntity getEntity() {
        return entity;
    }

    public void setEntity(AbstractEntity entity) {
        this.entity = entity;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Tour getOverlapsTour() {
        return overlapsTour;
    }

    public void setOverlapsTour(Tour overlapsTour) {
        this.overlapsTour = overlapsTour;
    }

    public DateTimeRange getOverlapsRange() {
        return overlapsRange;
    }

    public void setOverlapsRange(DateTimeRange overlapsRange) {
        this.overlapsRange = overlapsRange;
    }
}
