import { useState, useMemo } from 'react';
import Helmet from 'react-helmet';
import { useActivities } from 'hooks/Activities';
import URLS from 'URLS';
import { Link } from 'react-router-dom';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Typography, Button } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Pagination from 'components/layout/Pagination';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';
import ActivityCard from 'components/layout/ActivityCard';
import Container from 'components/layout/Container';
import MasonryGrid from 'components/layout/MasonryGrid';
import SearchBar from 'components/inputs/SearchBar';
import { Activity } from 'types/Types';

const useStyles = makeStyles((theme) => ({
  top: {
    width: '100%',
    padding: theme.spacing(4, 2),
  },
  suggestedActivities: {
    paddingTop: theme.spacing(6),
  },
  newActivities: {},
  wrapper: {
    marginTop: theme.spacing(5),
    textAlign: 'center',
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
    display: 'grid',
    gap: theme.spacing(1),
    gridTemplateColumns: '1fr auto',
    paddingBottom: theme.spacing(1),
  },
}));

export type ActivityFilters = Partial<Pick<Activity, 'title' | 'level'>> & {
  startDateAfter?: string;
  startDateBefore?: string;
};

const Activities = () => {
  const classes = useStyles();
  const [filters, setFilters] = useState<ActivityFilters>({});
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useActivities(filters);
  const activities = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !activities.length && !isFetching, [activities, isFetching]);

  return (
    <Navigation maxWidth={false} topbarVariant='dynamic'>
      <Helmet>
        <title>Aktiviteter</title>
      </Helmet>
      <div className={classes.top}>
        <div className={classes.searchWrapper}>
          <Typography align='center' className={classes.title} variant='h1'>
            Aktiviteter
          </Typography>
          <SearchBar updateFilters={setFilters} />
        </div>
      </div>
      <Container>
        <div className={classes.wrapper}>
          {/* <div className={classes.suggestedActivities}>
          <MasonryGrid>
            <div className={classes.title}>
              <Typography variant='h1'>Aktiviteter nær deg</Typography>
            </div>
            <ActivityCard />
          </MasonryGrid>
        </div> */}
          <div className={classes.newActivities}>
            <div className={classes.row}>
              <Typography align='left' className={classes.subtitle} variant='h2'>
                Nytt
              </Typography>
              <Button color='primary' component={Link} to={URLS.ADMIN_ACTIVITIES} variant='outlined'>
                Opprett aktivitet
              </Button>
            </div>
            <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
              <MasonryGrid>
                {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen aktiviteter'} />}
                {activities.map((activity) => (
                  <ActivityCard activity={activity} key={activity.id} />
                ))}
              </MasonryGrid>
            </Pagination>
          </div>
        </div>
      </Container>
    </Navigation>
  );
};

export default Activities;
