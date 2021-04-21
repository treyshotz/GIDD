import { useMemo } from 'react';
import Helmet from 'react-helmet';
import { Link } from 'react-router-dom';
import URLS from 'URLS';
import { useIsAuthenticated } from 'hooks/User';
import { useActivities } from 'hooks/Activities';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Typography, Button } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import image from 'assets/img/DefaultBackground.jpg';
import Pagination from 'components/layout/Pagination';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';
import ActivityCard from 'components/layout/ActivityCard';
import Container from 'components/layout/Container';
import MasonryGrid from 'components/layout/MasonryGrid';

const useStyles = makeStyles((theme) => ({
  header: {
    marginTop: theme.spacing(1),
  },
  img: {
    color: theme.palette.common.white,
    background: `${theme.palette.colors.gradient}, url(${image}) center center/cover no-repeat scroll`,
    height: '100vh',
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    boxShadow: 'inset 0 0 0 1000px rgba(0, 0, 0, 0.2)',
    objectFit: 'contain',
  },
  activityContainer: {
    textAlign: 'center',
    paddingTop: theme.spacing(2),
  },
  btnGroup: {
    display: 'flex',
    gap: theme.spacing(1),
  },
}));

const Landing = () => {
  const classes = useStyles();
  const isAuthenticated = useIsAuthenticated();
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useActivities();
  const activities = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !activities.length && !isFetching, [activities, isFetching]);

  return (
    <Navigation maxWidth={false}>
      <Helmet>
        <title>Forsiden - GIDD</title>
      </Helmet>
      <div className={classes.img}>
        <Typography align='center' className={classes.header} color='inherit' gutterBottom variant='h1'>
          GIDD
        </Typography>
        <Typography align='center' color='inherit' variant='h3'>
          Det er bare å GIDDe
        </Typography>
        <div className={classes.btnGroup}>
          {!isAuthenticated && (
            <>
              <Button component={Link} to={URLS.LOGIN} variant='outlined'>
                Logg inn
              </Button>
              <Button component={Link} to={URLS.SIGNUP} variant='outlined'>
                Registrer deg
              </Button>
            </>
          )}
        </div>
      </div>
      <Container className={classes.activityContainer}>
        <Typography variant='h1'>Nye aktiviteter</Typography>
        <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
          <MasonryGrid>
            {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen aktiviteter'} />}
            {activities.map((activity) => (
              <ActivityCard activity={activity} key={activity.id} />
            ))}
          </MasonryGrid>
        </Pagination>
      </Container>
    </Navigation>
  );
};

export default Landing;
