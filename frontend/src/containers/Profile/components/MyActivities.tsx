import { useState, useMemo } from 'react';
import { useMyParticipatingActivities } from 'hooks/Activities';

// Material UI
import { makeStyles } from '@material-ui/core/styles';
import { Button, ButtonGroup, Collapse } from '@material-ui/core';

// Project components
import Pagination from 'components/layout/Pagination';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';
import ActivityCard from 'components/layout/ActivityCard';
import Calendar from 'components/miscellaneous/Calendar';

const useStyles = makeStyles((theme) => ({
  buttons: {
    marginBottom: theme.spacing(1),
  },
  list: {
    display: 'grid',
    gap: theme.spacing(1),
    gridTemplateColumns: '1fr 1fr',
    [theme.breakpoints.down('md')]: {
      gridTemplateColumns: '1fr',
    },
  },
}));

export type MyActivitiesProps = {
  past?: boolean;
};

const MyActivities = ({ past = false }: MyActivitiesProps) => {
  const classes = useStyles();
  const [showCalendar, setShowCalendar] = useState(false);
  const filters = useMemo(
    () => ({
      [past ? 'registrationStartDateBefore' : 'registrationStartDateAfter']: new Date().toISOString(),
    }),
    [past],
  );
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useMyParticipatingActivities(filters);
  const activities = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !activities.length && !isFetching, [activities, isFetching]);

  return (
    <>
      <ButtonGroup aria-label='Set calendar or list' className={classes.buttons}>
        <Button onClick={() => setShowCalendar(false)} variant={showCalendar ? 'outlined' : 'contained'}>
          Liste
        </Button>
        <Button onClick={() => setShowCalendar(true)} variant={showCalendar ? 'contained' : 'outlined'}>
          Kalender
        </Button>
      </ButtonGroup>
      <Collapse in={!showCalendar}>
        <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
          {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen aktiviteter'} />}
          <div className={classes.list}>
            {activities.map((activity) => (
              <ActivityCard activity={activity} fullHeight key={activity.id} />
            ))}
          </div>
        </Pagination>
      </Collapse>
      <Collapse in={showCalendar}>
        <Calendar activities={activities} />
      </Collapse>
    </>
  );
};

export default MyActivities;
