import { useMemo } from 'react';
import Helmet from 'react-helmet';
import { useActivities } from 'hooks/Activities';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Pagination from 'components/layout/Pagination';
import Paper from 'components/layout/Paper';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';
import ActivityCard from 'components/layout/ActivityCard';

import MasonryGrid from 'components/layout/MasonryGrid';
import SearchBar from 'components/inputs/SearchBar';

const useStyles = makeStyles((theme) => ({
  list: {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr 1fr',
    gap: theme.spacing(0, 1),
    [theme.breakpoints.down('lg')]: {
      gap: theme.spacing(1),
      gridTemplateColumns: '1fr',
    },
  },
  first: {
    gridColumn: 'span 3',
    [theme.breakpoints.down('lg')]: {
      gridColumn: 'span 1',
    },
  },
  suggestedActivities: {
    paddingTop: theme.spacing(6),
  },
  newActivities: {},
  title: {
    textAlign: 'left',
    paddingBottom: theme.spacing(2),
  },
  wrapper: {
    marginTop: theme.spacing(5),
    textAlign: 'center',
  },
  searchWrapper: {
    display: 'flex',
    width: '90%',
    maxWidth: 600,
    maxHeight: '60px',
    margin: theme.spacing(2, 'auto'),
    gap: theme.spacing(1),
  },
}));

const Activities = () => {
  const classes = useStyles();
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useActivities();
  const activities = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !activities.length, [activities]);

  return (
    <Navigation topbarVariant='dynamic'>
      <Helmet>
        <title>Aktiviteter</title>
      </Helmet>
      <div className={classes.wrapper}>
        <div className={classes.searchWrapper}>
          <SearchBar />
        </div>
        {/* <div className={classes.suggestedActivities}>
          <MasonryGrid>
            <div className={classes.title}>
              <Typography variant='h1'>Aktiviteter nær deg</Typography>
            </div>
            <ActivityCard />
          </MasonryGrid>
        </div> */}
        <div className={classes.newActivities}>
          <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
            <MasonryGrid>
              <div className={classes.title}>
                <Typography variant='h1'>Nye Aktiviteter</Typography>
              </div>
              {isEmpty && <NotFoundIndicator header='Fant ingen aktiviteter' />}
              {error && <Paper>{error.message}</Paper>}
              {activities.map((activity) => (
                <ActivityCard activity={activity} key={activity.id} />
              ))}
            </MasonryGrid>
          </Pagination>
        </div>
      </div>
    </Navigation>
  );
};

export default Activities;
