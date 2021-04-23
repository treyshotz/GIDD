import { useState, useMemo } from 'react';
import Helmet from 'react-helmet';
import classnames from 'classnames';
import { useActivities } from 'hooks/Activities';
import URLS from 'URLS';
import { Link } from 'react-router-dom';
import { useIsAuthenticated } from 'hooks/User';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Typography, Button, ButtonGroup, Collapse, Hidden } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Pagination from 'components/layout/Pagination';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';
import Calendar from 'components/miscellaneous/Calendar';
import ActivityCard from 'components/layout/ActivityCard';
import MasonryGrid from 'components/layout/MasonryGrid';
import SearchBar from 'components/inputs/SearchBar';
import { Activity } from 'types/Types';

const useStyles = makeStyles((theme) => ({
  top: {
    width: '100%',
    padding: theme.spacing(4, 2),
  },
  grid: {
    display: 'grid',
    gap: theme.spacing(2),
    alignItems: 'self-start',
    [theme.breakpoints.down('md')]: {
      gap: theme.spacing(1),
    },
  },
  root: {
    gridTemplateColumns: '300px 1fr',
    [theme.breakpoints.down('xl')]: {
      gridTemplateColumns: '1fr',
    },
  },
  searchWrapper: {
    maxWidth: 700,
    margin: 'auto',
  },
  title: {
    color: theme.palette.getContrastText(theme.palette.colors.topbar),
    paddingBottom: theme.spacing(2),
  },
  subtitle: {
    margin: 'auto 0',
  },
  row: {
    display: 'flex',
    justifyContent: 'flex-end',
  },
  filterRow: {
    gridTemplateColumns: '1fr auto',
  },
}));

export type ActivityFilters = Partial<Pick<Activity, 'title' | 'level'>> & {
  startDateAfter?: string;
  startDateBefore?: string;
};

const Activities = () => {
  const classes = useStyles();
  const isAuthenticated = useIsAuthenticated();
  const [filters, setFilters] = useState<ActivityFilters>({});
  const [showCalendar, setShowCalendar] = useState(false);
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useActivities(filters);
  const activities = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !activities.length && !isFetching, [activities, isFetching]);

  return (
    <Navigation topbarVariant='dynamic'>
      <Helmet>
        <title>Aktiviteter</title>
      </Helmet>
      <div className={classes.top}>
        <div className={classes.searchWrapper}>
          <Typography align='center' className={classes.title} variant='h1'>
            Aktiviteter
          </Typography>
        </div>
      </div>
      <div className={classnames(classes.grid, classes.root)}>
        <div className={classes.grid}>
          <div className={classnames(classes.grid, classes.filterRow)}>
            <Typography align='left' className={classes.subtitle} variant='h2'>
              Filtrer
            </Typography>
            <Hidden xlUp>
              {isAuthenticated && (
                <Button color='primary' component={Link} to={URLS.ADMIN_ACTIVITIES} variant='text'>
                  Opprett aktivitet
                </Button>
              )}
            </Hidden>
          </div>
          <ButtonGroup aria-label='Set calendar or list' fullWidth>
            <Button onClick={() => setShowCalendar(false)} variant={showCalendar ? 'outlined' : 'contained'}>
              Liste
            </Button>
            <Button onClick={() => setShowCalendar(true)} variant={showCalendar ? 'contained' : 'outlined'}>
              Kalender
            </Button>
          </ButtonGroup>
          <SearchBar updateFilters={setFilters} />
        </div>
        <div>
          <Hidden xlDown>
            <div className={classes.row}>
              {isAuthenticated && (
                <Button color='primary' component={Link} to={URLS.ADMIN_ACTIVITIES} variant='text'>
                  Opprett aktivitet
                </Button>
              )}
            </div>
          </Hidden>
          <Collapse in={!showCalendar}>
            <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
              <MasonryGrid>
                {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen aktiviteter'} />}
                {activities.map((activity) => (
                  <ActivityCard activity={activity} key={activity.id} />
                ))}
              </MasonryGrid>
            </Pagination>
          </Collapse>
          <Collapse in={showCalendar}>
            <Calendar activities={activities} />
          </Collapse>
        </div>
      </div>
    </Navigation>
  );
};

export default Activities;
