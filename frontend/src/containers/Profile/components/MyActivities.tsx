import { useMemo, Fragment } from 'react';
import { useMyParticipatingActivities } from 'hooks/Activities';

// Material UI
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';

// Project components
import Pagination from 'components/layout/Pagination';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';

const useStyles = makeStyles((theme) => ({
  list: {
    display: 'grid',
    gap: theme.spacing(1),
  },
}));

const MyActivities = () => {
  const classes = useStyles();
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useMyParticipatingActivities();
  const isEmpty = useMemo(() => (data !== undefined ? !data.pages.some((page) => Boolean(page.results.length)) : false), [data]);

  return (
    <>
      {isEmpty && <NotFoundIndicator header='Fant ingen kommende aktiviteter' />}
      {error && error.detail}
      {data !== undefined && (
        <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
          <div className={classes.list}>
            {data.pages.map((page, i) => (
              <Fragment key={i}>
                {page.results.map((activity) => (
                  <Typography key={activity.activityId} variant='h2'>
                    - {activity.title}
                  </Typography>
                ))}
              </Fragment>
            ))}
          </div>
        </Pagination>
      )}
    </>
  );
};

export default MyActivities;
