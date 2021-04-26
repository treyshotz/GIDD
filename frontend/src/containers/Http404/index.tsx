import Helmet from 'react-helmet';
import { Link } from 'react-router-dom';
import URLS from 'URLS';
import { useIsAuthenticated } from 'hooks/User';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

// Project Components
import Logo from 'components/miscellaneous/Logo';
import Navigation from 'components/navigation/Navigation';

const useStyles = makeStyles((theme) => ({
  img: {
    width: '100%',
    maxHeight: '70vh',
    objectFit: 'contain',
  },
  imgPadding: {
    paddingTop: theme.spacing(3),
  },
  buttons: {
    margin: theme.spacing(2, 'auto'),
    display: 'grid',
    gap: theme.spacing(1),
    maxWidth: 200,
  },
  logo: {
    minWidth: '250px',
    width: '46%',
    maxWidth: '100%',
    height: 'auto',
    margin: theme.spacing(5, 'auto'),
    [theme.breakpoints.down('md')]: {
      minWidth: '200px',
    },
  },
}));

const Http404 = () => {
  const classes = useStyles();
  const isAuthenticated = useIsAuthenticated();

  return (
    <Navigation topbarVariant='filled'>
      <Helmet>
        <title>404</title>
      </Helmet>
      <Logo className={classes.logo} darkColor={'white'} lightColor={'black'} />
      <Typography align='center' variant='h2'>
        {isAuthenticated ? 'Du er innlogget, men vi kunne fremdeles ikke finne siden :(' : 'Kunne ikke finne siden'}
      </Typography>
      <div className={classes.buttons}>
        <Button color='primary' component={Link} to={URLS.LANDING}>
          Til forsiden
        </Button>
        {!isAuthenticated && (
          <Button color='primary' component={Link} to={URLS.LOGIN} variant='outlined'>
            Logg inn
          </Button>
        )}
      </div>
    </Navigation>
  );
};

export default Http404;
