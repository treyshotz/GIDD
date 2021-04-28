import { useState, useMemo } from 'react';
import { useMyParticipatingActivities, useMyLikedActivities } from 'hooks/Activities';

// Material UI
import { makeStyles } from '@material-ui/core/styles';
import { Button, ButtonGroup, Collapse } from '@material-ui/core';

// Project components
import Pagination from 'components/layout/Pagination';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';
import ActivityCard from 'components/layout/ActivityCard';
import Calendar from 'components/miscellaneous/Calendar';
import ActivitiesMap from 'components/miscellaneous/ActivitiesMap';

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
  type: 'future' | 'past' | 'favourites';
  userId?: string;
};

const MyActivities = ({ type, userId }: MyActivitiesProps) => {
  const classes = useStyles();
  const [view, setView] = useState<'list' | 'calendar' | 'map'>('list');
  const filters = useMemo(
    () =>
      type === 'favourites'
        ? {}
        : {
            [type === 'past' ? 'registrationStartDateBefore' : 'registrationStartDateAfter']: new Date().toISOString(),
          },
    [type],
  );
  const useHook = type === 'favourites' ? useMyLikedActivities : useMyParticipatingActivities;
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useHook(userId, filters);
  const activities = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !activities.length && !isFetching, [activities, isFetching]);

  return (
    <>
      <ButtonGroup aria-label='Set calendar or list' className={classes.buttons}>
        <Button onClick={() => setView('list')} variant={view === 'list' ? 'contained' : 'outlined'}>
          Liste
        </Button>
        <Button onClick={() => setView('calendar')} variant={view === 'calendar' ? 'contained' : 'outlined'}>
          Kalender
        </Button>
        <Button onClick={() => setView('map')} variant={view === 'map' ? 'contained' : 'outlined'}>
          Kart
        </Button>
      </ButtonGroup>
      <Collapse in={view === 'list'}>
        <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
          {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen aktiviteter'} />}
          <div className={classes.list}>
            {activities.map((activity) => (
              <ActivityCard activity={activity} fullHeight key={activity.id} />
            ))}
          </div>
        </Pagination>
      </Collapse>
      <Collapse in={view === 'calendar'} mountOnEnter>
        <Calendar activities={activities} />
      </Collapse>
      <Collapse in={view === 'map'} mountOnEnter>
        <ActivitiesMap hookArgs={filters} useHook={useHook} userId={userId} />
      </Collapse>
    </>
  );
};

export default MyActivities;
